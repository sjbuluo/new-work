package com.sun.health.newwork.interview.java.network.thread;

import java.util.concurrent.TimeUnit;

public class InterruptRunnable implements Runnable {


    @Override
    public void run() {
        try {
            System.out.println("sleep 前");
            TimeUnit.SECONDS.sleep(10);
            System.out.println("sleep 后");
        } catch (InterruptedException e) {
//            e.printStackTrace();
            System.out.println("线程被中断");
        }
        Thread.interrupted();
    }
}
