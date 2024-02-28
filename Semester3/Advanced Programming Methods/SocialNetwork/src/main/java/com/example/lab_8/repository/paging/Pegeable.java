package com.example.lab_8.repository.paging;

public class Pegeable {
    private int pageNumber;
    private int pageSize;

    public Pegeable(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }
}
