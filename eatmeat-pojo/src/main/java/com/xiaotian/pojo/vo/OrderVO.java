package com.xiaotian.pojo.vo;

import com.xiaotian.pojo.bo.ShopCatBO;

import java.util.List;

/**
 * @author xiaotian
 * @description
 * @date 2022-01-23 11:33
 */
public class OrderVO {
    private String orderId;
    private MerchantOrdersVO merchantOrdersVO;
    private List<ShopCatBO> toBeRemoveShopCat;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerchantOrdersVO getMerchantOrdersVO() {
        return merchantOrdersVO;
    }

    public void setMerchantOrdersVO(MerchantOrdersVO merchantOrdersVO) {
        this.merchantOrdersVO = merchantOrdersVO;
    }

    public List<ShopCatBO> getToBeRemoveShopCat() {
        return toBeRemoveShopCat;
    }

    public void setToBeRemoveShopCat(List<ShopCatBO> toBeRemoveShopCat) {
        this.toBeRemoveShopCat = toBeRemoveShopCat;
    }
}