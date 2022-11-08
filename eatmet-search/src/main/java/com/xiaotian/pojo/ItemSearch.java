package com.xiaotian.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import javax.persistence.Column;

/**
 * @author xiaotian
 * @description
 * @date 2022-11-03 11:02
 */
@Document(indexName = "eat-meat", type = "_doc")
public class ItemSearch {

    /**
     * 商品主键id
     */
    @Id
    @Field(store = true, index = false)
    private String itemId;

    /**
     * 商品名称 商品名称
     */
    @Field(store = true, index = true)
    private String itemName;

    /**
     * 累计销售 累计销售
     */
    @Field(store = true)
    private Integer sellCounts;

    @Field(store = true)
    private Integer price;

    @Field(store = true, index = false)
    private String imageUrl;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getSellCounts() {
        return sellCounts;
    }

    public void setSellCounts(Integer sellCounts) {
        this.sellCounts = sellCounts;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "ItemSearch{" +
                "itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", sellCounts=" + sellCounts +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
