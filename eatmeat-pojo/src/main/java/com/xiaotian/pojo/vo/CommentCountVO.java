package com.xiaotian.pojo.vo;

import java.util.List;

/**
 * @author xiaotian
 * @description
 * @date 2021-12-06 22:02
 */
public class CommentCountVO {

    private Integer goodCounts;
    private Integer normalCounts;
    private Integer badCounts;
    private Integer totalCounts;

    public Integer getBadCounts() {
        return badCounts;
    }

    public CommentCountVO(Integer goodCounts, Integer normalCounts, Integer badCounts, Integer totalCounts) {
        this.goodCounts = goodCounts;
        this.normalCounts = normalCounts;
        this.badCounts = badCounts;
        this.totalCounts = totalCounts;
    }

    public Integer getGoodCounts() {
        return goodCounts;
    }

    public void setGoodCounts(Integer goodCounts) {
        this.goodCounts = goodCounts;
    }

    public Integer getNormalCounts() {
        return normalCounts;
    }

    public void setNormalCounts(Integer normalCounts) {
        this.normalCounts = normalCounts;
    }

    public Integer getTotalCounts() {
        return totalCounts;
    }

    public void setTotalCounts(Integer totalCounts) {
        this.totalCounts = totalCounts;
    }
}
