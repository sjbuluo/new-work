package com.sun.health.newwork.interview.java.network.thread;

import java.util.concurrent.TimeUnit;

/**
 * Created by 华硕 on 2019-05-21.
 */
public class CounterTask {

    private int i = 1;

    public synchronized int printOdd() throws InterruptedException {
        if (i % 2 == 1) {
            TimeUnit.MILLISECONDS.sleep(100);
            return i++;
        } else {
            return -1;
        }
    }

    public synchronized int printEven() throws InterruptedException {
        if (i % 2 == 0) {
            TimeUnit.MILLISECONDS.sleep(100);
            return i++;
        } else {
            return -1;
        }
    }

}
