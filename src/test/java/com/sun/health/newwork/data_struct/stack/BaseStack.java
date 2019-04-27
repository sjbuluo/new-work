package com.sun.health.newwork.data_struct.stack;

import org.junit.Test;

import java.util.Arrays;

public class BaseStack {

    private int index;

    private Object[] data;

    public BaseStack() {
    }

    public BaseStack(int size) {
        data = new Object[size];
    }

    public boolean isEmpty() {
        return index == 0;
    }

    public void push(Object obj) {
        if(!isFull()) {
            data[index++] = obj;
        } else {
            throw new RuntimeException("栈已满");
        }
    }

    public Object pop() {
        if(!isEmpty()) {
            Object obj = data[--index];
            data[index] = null;
            return obj;
        } else {
            return null;
        }
    }

    public Object peek() {
        if (!isEmpty()) {
            return data[index - 1];
        } else {
            return null;
        }
    }

    public boolean isFull() {
        return index == data.length;
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }


}
