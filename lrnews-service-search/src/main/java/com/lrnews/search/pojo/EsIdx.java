package com.lrnews.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "EsIdx", type = "_doc") // 7.x版本后不再支持type属性
public class EsIdx {

    @Id
    Long indexId;

    @Field
    private String indexName;

    @Field
    private Integer counts;

    @Field
    private Float price;

    @Field
    private String infos;

    public Long getIndexId() {
        return indexId;
    }

    public void setIndexId(Long indexId) {
        this.indexId = indexId;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getInfos() {
        return infos;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }

    @Override
    public String toString() {
        return "EsIdx{" +
                "indexId=" + indexId +
                ", indexName='" + indexName + '\'' +
                ", counts=" + counts +
                ", price=" + price +
                ", infos='" + infos + '\'' +
                '}';
    }
}
