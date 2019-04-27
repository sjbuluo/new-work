package com.sun.health.newwork.thread;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleExecutorServiceTest {

    @Test
    public void test1() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        
    }

}
