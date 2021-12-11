package com.xiaotian.pojo.vo;

import com.xiaotian.pojo.Items;
import com.xiaotian.pojo.ItemsImg;
import com.xiaotian.pojo.ItemsParam;
import com.xiaotian.pojo.ItemsSpec;

import java.util.List;

/**
 * @author xiaotian
 * @description 商品信息展示
 * @date 2021-12-11 19:07
 */
public class ItemInfoVO {

    private Items item;

    private ItemsParam itemParams;

    private List<ItemsSpec> itemSpecList;

    private List<ItemsImg> itemImgList;

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public ItemsParam getItemParams() {
        return itemParams;
    }

    public void setItemParams(ItemsParam itemParams) {
        this.itemParams = itemParams;
    }

    public List<ItemsSpec> getItemSpecList() {
        return itemSpecList;
    }

    public void setItemSpecList(List<ItemsSpec> itemSpecList) {
        this.itemSpecList = itemSpecList;
    }

    public List<ItemsImg> getItemImgList() {
        return itemImgList;
    }

    public void setItemImgList(List<ItemsImg> itemImgList) {
        this.itemImgList = itemImgList;
    }
}
