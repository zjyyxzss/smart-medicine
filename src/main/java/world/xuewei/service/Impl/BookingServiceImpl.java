package world.xuewei.service.Impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import world.xuewei.dao.BookMapper;
import world.xuewei.dao.DoctorScheduleMapper;
import world.xuewei.dto.RespResult;
import world.xuewei.entity.DoctorSchedule;
import world.xuewei.service.BookingService;

import world.xuewei.entity.BookingRecord;

import java.util.UUID;


/**
 * 预约服务实现类
 *
 *
 */
@Slf4j
@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private DoctorScheduleMapper doctorScheduleMapper;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String STOCK_KEY_PREFIX = "stock:schedule:";

    @Override
    public void initStock(Long scheduleId) {
        //1.从数据库中查询库存
        DoctorSchedule doctorSchedule = doctorScheduleMapper.selectById(scheduleId);
        if (doctorSchedule == null) {
            return;
        }
        //2.将库存写入Redis
        redisTemplate.opsForValue().set(STOCK_KEY_PREFIX + scheduleId, String.valueOf(doctorSchedule.getAvailableStock()));
        log.info("排班ID:"+scheduleId+ " 库存已预热到 Redis!");
    }
    @PostConstruct
    public void initAllStockOnStartup() {
        log.info("--- 正在执行所有库存预热 ---");
        initStock(1L);
        log.info("--- 库存预热完成 ---");
    }
    /**
     * 用户预约医生
     *
     * @param userId     用户ID
     * @param scheduleId 排班ID
     * @return 预约结果，包含成功或失败信息
     */

    @Override
    @Transactional
    public RespResult book(Long userId, Long scheduleId) {
        String stockKey = STOCK_KEY_PREFIX + scheduleId;
        //1.Redis 原子扣减库存
        Long currentStock = redisTemplate.opsForValue().decrement(stockKey);
        if (currentStock == null) {
            return RespResult.fail("排班不存在");
        }
        if (currentStock < 0) {
            redisTemplate.opsForValue().increment(stockKey);
            return RespResult.fail("排班已预约完");
        }
        //2.执行数据库扣减库存
        try {
            int rows = doctorScheduleMapper.decreaseStockBySql(scheduleId);

            if (rows == 0) {
                // 数据库扣减失败！(可能是多线程同时抢，库存只剩 0，但 Redis 因为延迟多放行了一个)
                // 此时 Redis 已经扣了，需要回滚！
                redisTemplate.opsForValue().increment(stockKey);
                // 抛出 RuntimeException，触发 @Transactional 回滚
                throw new RuntimeException("数据库库存已不足，抢号失败！");
            }
            DoctorSchedule doctorSchedule = doctorScheduleMapper.selectById(scheduleId);
            //3.3创建订单
            // 2.2 数据库扣减成功，创建订单
            BookingRecord record = new BookingRecord();
            // ⚠️ 确保你的 User 实体类中 getId() 方法返回 Long
            if (doctorSchedule != null) {
                record.setUserId(userId.intValue());
                record.setDoctorId(doctorSchedule.getDoctorId());
                record.setScheduleId(scheduleId.intValue());

                record.setAppointmentDate(doctorSchedule.getScheduleDate());
                record.setAppointmentTimeSlot(doctorSchedule.getTimeSlot());
            }
            record.setStatus(0); // 待处理状态
            record.setBookingNo("B" + UUID.randomUUID().toString().replace("-", ""));
            bookMapper.insert(record);
            //3.4返回
            return RespResult.success("预约成功!订单号:" + record.getBookingNo());
        } catch (Exception e) {
            //4.1 回滚Redis库存
            redisTemplate.opsForValue().increment(stockKey);
            //4.3 返回失败结果
            throw new RuntimeException("预约失败", e);
        }
    }
}
