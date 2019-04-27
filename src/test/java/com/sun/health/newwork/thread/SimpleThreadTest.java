package com.sun.health.newwork.thread;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimpleThreadTest {

    @Test
    public void test1() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("在新线程中运行");
            }
        };
        thread.start();
        System.out.println("在主程序中运行");
    }

    @Test
    public void test2() throws InterruptedException {
        PrintNumThread printNumThread = new PrintNumThread();
        PrintCharThread printCharThread = new PrintCharThread();
        Object lock = new Object();
//        printNumThread.setPrintCharThread(lock);
//        printCharThread.setPrintNumThread(lock);
        printNumThread.setLock(lock);
        printCharThread.setLock(lock);
        printCharThread.start();
        printNumThread.start();
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void test3() throws InterruptedException {
        Object lock = new Object();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    try {
                        for (int i = 0; i < 26; i++) {
                            lock.wait();
                            System.out.print((char)(i + 'A') + " ");
                            lock.notify();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    try {
                        for (int i = 0; i < 53; ) {
                            System.out.print(++i);
                            System.out.print(++i);
                            lock.notify();
                            lock.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        executorService.shutdown();
        TimeUnit.SECONDS.sleep(1);
    }

}
