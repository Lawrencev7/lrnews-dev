package com.lrnews.vo;

import com.github.pagehelper.PageInfo;

import java.util.List;

public class PagedGridVO {
    private int page;            // 当前页数
    private long pages;            // 总页数
    private long records;        // 总记录数
    private List<?> rows;        // 每行显示的内容

    private PagedGridVO() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }

    public void setTotal(int total) {
        this.pages = total;
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

    public static PagedGridVO getPagedGrid(List<?> sourceList, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(sourceList);
        PagedGridVO gridResult = new PagedGridVO();
        gridResult.setRows(sourceList);
        gridResult.setPage(page);
        gridResult.setRecords(pageList.getTotal());
        gridResult.setPages(pageList.getPages());
        return gridResult;
    }
}
