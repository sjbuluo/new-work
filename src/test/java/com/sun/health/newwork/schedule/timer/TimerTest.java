package com.sun.health.newwork.schedule.timer;

import org.junit.Test;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TimerTest {

    @Test
    public void test1() throws InterruptedException {
        TimerTask timerTask = new TimerTask() {
            private int id = 0;
            @Override
            public void run() {
                System.out.println(id++ +" Task run: " + new Date());
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 2000,1000);
        TimeUnit.SECONDS.sleep(15);
    }

}
