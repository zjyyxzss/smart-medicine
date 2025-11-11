package world.xuewei.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import world.xuewei.entity.Medicine; // 导入你的 MySQL 实体类

// 必须的 Getter/Setter/构造函数
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 药品在 Elasticsearch 中的 Document 模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "medicine_index") // 1. 指定索引名称（ES 中的 "表名"）
public class MedicineDocument {

    @Id // 2. 标记为主键
    private Long id; // 对应数据库的 ID

    /**
     * 药品名称 (用于全文检索)
     * type = FieldType.Text: 表示该字段需要被分词，用于搜索。
     * analyzer = "standard": 指定使用标准分词器。
     */
    @Field(type = FieldType.Text)
    private String name;

    /**
     * 药品功能/主治病症 (用于全文检索)
     */
    @Field(type = FieldType.Text)
    private String functions;

    /**
     * 药品图片 (不分词)
     * type = FieldType.Keyword: 表示该字段不分词，用于精确匹配或排序。
     */
    @Field(type = FieldType.Keyword, index = false) // index = false 表示此字段仅存储，不用于搜索
    private String image;

    /**
     * 构造函数：用于将 MySQL 实体 转换为 ES Document
     */
    public MedicineDocument(Medicine medicine) {
        this.id = medicine.getId().longValue(); // 确保类型匹配
        this.name = medicine.getMedicineName();
        this.functions = medicine.getMedicineInfo();
        this.image = medicine.getImgPath();
    }
}