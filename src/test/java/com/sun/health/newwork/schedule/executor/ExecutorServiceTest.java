package com.sun.health.newwork.schedule.executor;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.*;

public class ExecutorServiceTest {

    /**
     *
     * @throws InterruptedException
     */
    @Test
    public void test1() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 20; i++) {
            final int id = i;
            executorService.execute(() -> {
                System.out.println(id + " Task run in executorService: " + new Date());
            });
        }
        executorService.shutdown();
        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * 在指定时间之后执行一次任务
     * @throws InterruptedException
     */
    @Test
    public void test2() throws InterruptedException {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            System.out.println(" Task run in ScheduleExecutorService: " + new Date());
        }, 1, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 以上一个任务开始的时候开始计时 计时完成后若上一个任务完成 则开始 若上一个任务未完成则等待上一个任务完成后继续执行
     * @throws InterruptedException
     */
    @Test
    public void test3() throws InterruptedException {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            System.out.println(" Task run in ScheduleExecutorService: " + new Date());
        }, 1, 1, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 以上一个任务完成开始计时 等待指定时间后 开始执行下一个任务
     * @throws InterruptedException
     */
    @Test
    public void test4() throws InterruptedException {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(() -> {
            System.out.println(" Task run in ScheduleExecutorService: " + new Date());
        }, 1, 1, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void test5() throws InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("在线程工厂中修改");
                        r.run();
                    }
                });
            }
        });
        executorService.execute(() -> {
            System.out.println(" Task run in ScheduleExecutorService: " + new Date());
        });
        executorService.shutdown();
        TimeUnit.SECONDS.sleep(5);
    }

}
