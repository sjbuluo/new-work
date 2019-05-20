package com.sun.health.newwork.interview.java.network.thread;

import org.omg.CORBA.INTERNAL;

import java.util.concurrent.Callable;

/**
 * Created by 华硕 on 2019-05-20.
 */
public class MultiThreadMaxFinder implements Callable<Integer> {

    private int[] data;

    private int start;

    private int end;

    public MultiThreadMaxFinder(int[] data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
    }

    @Override
    public Integer call() throws Exception {
        int max = Integer.MIN_VALUE;
        for (int i = start; i < end; i++) {
            if (max < data[i])
                max = data[i];
        }
        return max;
    }
}
