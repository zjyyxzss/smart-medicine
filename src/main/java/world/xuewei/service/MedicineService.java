package world.xuewei.service;

<<<<<<< HEAD
=======
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import world.xuewei.dao.MedicineDao;
import world.xuewei.entity.Medicine;
import world.xuewei.utils.Assert;
import world.xuewei.utils.BeanUtil;
import world.xuewei.utils.VariableNameUtils;
>>>>>>> 1251b11d1841a7f762a90ff77d621b552337bbf8

import world.xuewei.document.MedicineDocument;
import world.xuewei.dto.RespResult;
import world.xuewei.entity.Medicine;

import java.util.List;
import java.util.Map;

/**
<<<<<<< HEAD
 * 药品服务接口
 * 继承自定义的 IService
 */
public interface MedicineService extends IService<Medicine> { // <-- 确保继承 IService
=======
 * 药品服务类
 *
 *
 */
@Service
@Slf4j
public class MedicineService extends BaseService<Medicine> {
>>>>>>> 1251b11d1841a7f762a90ff77d621b552337bbf8

    /**
     * Day 7：同步数据到 ES
     */
    RespResult syncDrugsToEs();

    /**
<<<<<<< HEAD
     * Day 7：从 ES 搜索
     */
    List<MedicineDocument> searchDrugs(String keyword);
=======
         * 根据ID查询药品详情，并使用缓存
     * */
    @Override
    @Cacheable(value = "medicine", key = "#id")
    public Medicine getById(Serializable id) {
        log.info("正在从数据库查询药品详情,id={}", id);
        return medicineDao.selectById(id);
    }

    @Override
    public List<Medicine> query(Medicine o) {
        QueryWrapper<Medicine> wrapper = new QueryWrapper<>();
        if (Assert.notEmpty(o)) {
            Map<String, Object> bean2Map = BeanUtil.bean2Map(o);
            for (String key : bean2Map.keySet()) {
                if (Assert.isEmpty(bean2Map.get(key))) {
                    continue;
                }
                wrapper.eq(VariableNameUtils.humpToLine(key), bean2Map.get(key));
            }
        }
        return medicineDao.selectList(wrapper);
    }
>>>>>>> 1251b11d1841a7f762a90ff77d621b552337bbf8

    Map<String,?> getMedicineList(String nameValue, Integer page);
}