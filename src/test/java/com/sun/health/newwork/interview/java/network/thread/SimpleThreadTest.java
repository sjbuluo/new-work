package com.sun.health.newwork.interview.java.network.thread;

import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.sql.Time;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by 华硕 on 2019-05-20.
 */
public class SimpleThreadTest implements DigestResultPrinter {

    @Test
    public void test1() throws InterruptedException {
        DigestThread digestThread = new DigestThread("PHP.txt");
        digestThread.start();
        digestThread.join();
    }

    @Test
    public void test2() throws InterruptedException {
        Thread thread = new Thread(new DigestRunnable("PHP.txt"));
        thread.start();
        thread.join();
    }

    @Test
    public void test3() throws InterruptedException {
        Thread thread = new Thread(new CallbackDigestRunnable("PHP.txt", this));
        thread.start();
        thread.join();
    }

    @Test
    public void test4() throws ExecutionException, InterruptedException {
        Random random = new Random();
        int[] data = new int[100];
        for (int i = 0; i < 100; i++) {
            data[i] = random.nextInt(10000);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> leftFuture = executorService.submit(new MultiThreadMaxFinder(data, 0, data.length / 2));
        Future<Integer> rightFuture = executorService.submit(new MultiThreadMaxFinder(data, data.length / 2, data.length));
        System.out.println(Arrays.toString(data));
        System.out.println(Math.max(leftFuture.get(), rightFuture.get()));
    }

    @Test
    public void test5() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new NoSynchronziedRunnable("readme.txt"));
        executorService.submit(new NoSynchronziedRunnable("深入浅出SQL.txt"));
        executorService.submit(new NoSynchronziedRunnable("JAVA编程思想.txt"));
        executorService.submit(new NoSynchronziedRunnable("Java网络编程.txt"));
        executorService.submit(new NoSynchronziedRunnable("PHP.txt"));
        executorService.shutdown();
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void test6() throws InterruptedException {
        Thread thread = new Thread(new InterruptRunnable());
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("结束");
    }

    @Test
    public void test7() throws InterruptedException {
        CounterTask counterTask = new CounterTask();
        Thread oddThread = new Thread(() -> {
            while (true) {
                int i = -1;
                try {
                    i = counterTask.printOdd();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i == -1)
                    continue;
                if (i > 50) {
                    Thread.interrupted();
                }
                System.out.println("Thread odd: " + i);
            }
        });
        Thread evenThread = new Thread(() -> {
            while (true) {
                int i = -1;
                try {
                    i = counterTask.printEven();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i == -1)
                    continue;
                if (i > 50) {
                    Thread.interrupted();
                }
                System.out.println("Thread even: " + i);
            }
        });
        oddThread.start();
        evenThread.start();
        TimeUnit.SECONDS.sleep(100);
    }

    @Override
    public void printDigestResult(String filename, byte[] data) {
        System.out.println(filename + ":" + DatatypeConverter.printHexBinary(data));
    }
}
