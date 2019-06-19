package com.sun.health.newwork.netty.event_loop;

import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.oio.OioSocketChannel;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by 华硕 on 2019-06-18.
 */
public class SimpleEventLoopTest1 {

    @Test
    public void test1() throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(() -> {
            System.out.println("60秒之后执行");
        }, 10, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();
        TimeUnit.SECONDS.sleep(15);
    }


    @Test
    public void test2() throws InterruptedException {
        Channel channel = new OioSocketChannel();
        EventLoop eventExecutors = channel.eventLoop();
        eventExecutors.schedule(() -> {
            System.out.println("10秒后执行");
        }, 60, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(12);
        eventExecutors.shutdownGracefully().sync();

    }

}
