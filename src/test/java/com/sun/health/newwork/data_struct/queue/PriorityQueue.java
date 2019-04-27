package com.sun.health.newwork.data_struct.queue;

import java.util.Arrays;

/**
 * 优先级队列
 */
public class PriorityQueue {

    private int[] data;

    private int maxSize;

    private int itemCount;

    private int head;

    private int tail;

    public PriorityQueue(int maxSize) {
        this.maxSize = maxSize;
        this.data = new int[maxSize];
    }

    public PriorityQueue() {
        this(10);
    }

    public boolean isEmpty() {
        return itemCount == 0;
    }

    public boolean isFull() {
        return itemCount == maxSize;
    }

    public void insert(int i) {
        if(!isFull()) {
            itemCount++;
            int start = tail;
            int end = head < tail ? head + maxSize : head;
            boolean find = false;
            for (int j = start; j < end; j++) {
                if(data[j] < i) {
                    int current = j;
                    for (int k = end;k > j;) {
                        data[(k) % maxSize] = data[--k % maxSize];
                    }
                    data[current] = i;
                    find = true;
                    break;
                }
            }
            if(!find) {
                data[head] = i;
            }
            head = ++head % maxSize;
        } else {
            throw new RuntimeException("队列已满");
        }
    }

    public int remove() {
        if(!isEmpty()) {
            itemCount--;
            int result = data[tail];
            data[tail] = 0;
            tail = ++tail % maxSize;
            return result;
        } else {
            throw new RuntimeException("队列为空");
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}
