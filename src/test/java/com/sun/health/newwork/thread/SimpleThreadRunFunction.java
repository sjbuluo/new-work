package com.sun.health.newwork.thread;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class SimpleThreadRunFunction extends Thread {

    @Override
    public void run() {
        System.out.println("Run function in thread");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() throws InterruptedException {
        SimpleThreadRunFunction simpleThreadRunFunction = new SimpleThreadRunFunction();
        simpleThreadRunFunction.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("为止异常");
                System.out.println(t.getName());
                e.printStackTrace();
            }
        });
        simpleThreadRunFunction.start();
        simpleThreadRunFunction.join();
        System.out.println("主线程结束");
    }

}
