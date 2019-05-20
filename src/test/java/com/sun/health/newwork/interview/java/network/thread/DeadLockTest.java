package com.sun.health.newwork.interview.java.network.thread;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DeadLockTest {
    @Test
    public void test1() throws InterruptedException {
        List<Kuaizi> kuaizis = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            kuaizis.add(new Kuaizi());
        }
        List<Thinker> thinkers = new ArrayList<>();
        int size = kuaizis.size();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < size; i++) {
//            thinkers.add();
            executorService.submit(new Thinker(kuaizis.get(i % size), kuaizis.get((i + 1) % size), 0));
        }
        executorService.shutdown();
        TimeUnit.SECONDS.sleep(10);

    }
}
