package com.sun.health.newwork.data_struct.queue;

import org.junit.Test;

import java.util.Arrays;

public class UnidirectionalQueue {

    private Object[] data;

    private int maxSize;

    private int itemCount;

    private int head;

    private int tail;

    public UnidirectionalQueue(int maxSize) {
        this.maxSize = maxSize;
        this.data = new Object[maxSize];
    }

    public UnidirectionalQueue() {
        this(10);
    }

    public boolean isEmpty() {
        return this.itemCount == 0;
    }

    public boolean isFull() {
        return this.itemCount == this.maxSize;
    }

    public void insert(Object obj) {
        if(!isFull()) {
            this.data[this.head++ % maxSize] = obj;
            this.itemCount++;
        } else {
            throw new RuntimeException("队列已满");
        }
    }

    public Object remove() {
        if(!isEmpty()) {
            Object obj = this.data[tail];
            this.data[tail++ % maxSize] = null;
            this.itemCount--;
            return obj;
        } else {
            throw new RuntimeException("空队列");
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(this.data);
    }



}
