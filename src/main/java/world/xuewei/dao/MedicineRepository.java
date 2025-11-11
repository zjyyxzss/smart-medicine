package world.xuewei.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import world.xuewei.document.MedicineDocument;


/**
 * 药品 ES 索引的数据访问接口
 * 继承 ElasticsearchRepository
 */
public interface MedicineRepository extends ElasticsearchRepository<MedicineDocument, Long> {

    // Spring Data Elasticsearch 会根据方法名自动实现查询
    // 例如：根据药品名称模糊查询
    // List<DrugDocument> findByNameLike(String name);
}