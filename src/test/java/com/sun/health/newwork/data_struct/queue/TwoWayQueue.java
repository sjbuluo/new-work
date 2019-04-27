package com.sun.health.newwork.data_struct.queue;

import java.util.Arrays;

public class TwoWayQueue {

    private Object[] data;

    private int maxSize;

    private int itemCount;

    private int head;

    private int tail;

    public TwoWayQueue() {
        this(10);
    }

    public TwoWayQueue(int maxSize) {
        this.maxSize = maxSize;
        this.data = new Object[maxSize];
    }

    public boolean isEmpty() {
        return itemCount == 0;
    }

    public boolean isFull() {
        return itemCount == maxSize;
    }

    public void insertHead(Object obj) {
        if(!isFull()) {
            itemCount++;
            data[head++ % maxSize] = obj;
        } else {
            throw new RuntimeException("队列已满");
        }
    }

    public void insertTail(Object obj) {
        if(!isFull()) {
            if(isEmpty()) {
                itemCount++;
                data[head++ % maxSize] = obj;
            } else {
                itemCount++;
                tail = --tail < 0 ? maxSize + tail : tail;
                data[tail] = obj;
            }
        } else {
            throw new RuntimeException("队列已满");
        }
    }

    public Object removeHead() {
        if(!isEmpty()) {
            itemCount--;
            Object obj = data[head];
            data[head] = null;
            head = --head < 0 ? maxSize + head : head;
            return obj;
        } else {
            throw new RuntimeException("空队列");
        }
    }

    public Object removeTail() {
        if(!isEmpty()) {
            itemCount--;
            Object obj = data[tail];
            data[tail-- % maxSize] = null;
            return obj;
        } else {
            throw new RuntimeException("空队列");
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}
