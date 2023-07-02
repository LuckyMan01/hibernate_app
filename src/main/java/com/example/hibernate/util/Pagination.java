package com.example.hibernate.util;

public class Pagination {
    private int count;

    public Pagination(int init) {
        count = init;
    }

    public int get() {
        return count;
    }

    public void clear() {
        count = 0;
    }

    public int incrementAndGet() {
        return ++count;
    }

    public int decrementAndGet() {
        return --count;
    }

    public String toString() {
        return "" + count;
    }

}
