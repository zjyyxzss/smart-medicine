package world.xuewei.service;


import world.xuewei.document.MedicineDocument;
import world.xuewei.dto.RespResult;
import world.xuewei.entity.Medicine;

import java.util.List;
import java.util.Map;

/**
 * 药品服务接口
 * 继承自定义的 IService
 */
public interface MedicineService extends IService<Medicine> { // <-- 确保继承 IService

    /**
     * Day 7：同步数据到 ES
     */
    RespResult syncDrugsToEs();

    /**
     * Day 7：从 ES 搜索
     */
    List<MedicineDocument> searchDrugs(String keyword);

    Map<String,?> getMedicineList(String nameValue, Integer page);
}