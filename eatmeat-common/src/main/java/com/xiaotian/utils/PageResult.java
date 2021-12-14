package com.xiaotian.utils;

import java.util.List;

/**
 * @author xiaotian
 * @description 用来返回分页的数据格式
 * @date 2021-12-12 22:09
 */
public class PageResult {

    private int page;            // 当前页数
    private int total;            // 总页数
    private long records;        // 总记录数
    private List<?> rows;        // 每行显示的内容

    public PageResult() {
    }

    public PageResult(int page, int total, long records, List<?> rows) {
        this.page = page;
        this.total = total;
        this.records = records;
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getRecords() {
        return records;
    }

    public void setRecords(long records) {
        this.records = records;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
