package com.sun.health.newwork.thread;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.*;

public class SimpleCallableThread {

    @Test
    public void test1() throws ExecutionException, InterruptedException {
        FutureTask<Object> objectFutureTask = new FutureTask<>(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                TimeUnit.SECONDS.sleep(1);
                return 1;
            }
        });
        Random random = new Random();
        new Thread(objectFutureTask).start();
        while(!objectFutureTask.isDone() && !objectFutureTask.isCancelled()) {
            System.out.println("尚在执行");
            TimeUnit.MILLISECONDS.sleep(100);
            if(random.nextBoolean()) {
                objectFutureTask.cancel(true);
            }
        }
        if(objectFutureTask.isCancelled()) {
            System.out.println("FutureTask取消");
        }
        else if(objectFutureTask.isDone()) {
            System.out.println(objectFutureTask.get());
        }

    }

    @Test
    public void testCallable() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Object> submit = executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                TimeUnit.SECONDS.sleep(1);
                return 1;
            }
        });
        executorService.shutdown();
        Random random = new Random();
        while(!submit.isDone() && !submit.isCancelled()) {
            System.out.println("任务尚在执行");
            TimeUnit.MILLISECONDS.sleep(100);
            if(random.nextInt(100) < 5) {
                submit.cancel(true);
            }
        }
        if(submit.isCancelled()) {
            System.out.println("任意被取消");
        } else if(submit.isDone()) {
            System.out.println(submit.get());
        }
        System.out.println("结束");
    }

}
