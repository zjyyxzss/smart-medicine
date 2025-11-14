package world.xuewei.controller;

import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.*;
import world.xuewei.document.MedicineDocument;
=======
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
>>>>>>> 1251b11d1841a7f762a90ff77d621b552337bbf8
import world.xuewei.dto.RespResult;
import world.xuewei.entity.Medicine;
import world.xuewei.service.BaseService;
import world.xuewei.service.MedicineService;

import world.xuewei.service.MedicineService;

import java.util.List;


/**
 * 药品控制器
 *
 *
 */
@RestController
@RequestMapping("medicine")
public class MedicineController extends BaseController<Medicine> {
@Autowired
private MedicineService medicineService;


    @GetMapping("/{id}")
    public RespResult getById(@PathVariable("id") Integer id) {
        Medicine medicine = medicineService.getById(id);
        if (medicine == null) {
            return RespResult.notFound();
        }
        return RespResult.success(String.valueOf(medicine));
    }
<<<<<<< HEAD
    /**
     * [临时接口] 触发全量同步数据到 ES
     * (实际项目中，这通常由定时任务或数据管道完成)
     */
    @GetMapping("/sync-es")
    public RespResult syncToEs() {
        return medicineService.syncDrugsToEs();
    }
    /**
     * Day 7 核心：使用 ES 进行全文检索
     * @param keyword 搜索关键词
     */
    @GetMapping("/search-es")
    public RespResult searchEs(@RequestParam String keyword) {

        List<MedicineDocument> results = medicineService.searchDrugs(keyword);

        return RespResult.success(results.toString());
    }
=======
>>>>>>> 1251b11d1841a7f762a90ff77d621b552337bbf8



}
