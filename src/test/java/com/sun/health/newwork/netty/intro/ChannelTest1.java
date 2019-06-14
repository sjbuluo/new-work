package com.sun.health.newwork.netty.intro;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by 华硕 on 2019-06-14.
 */
public class ChannelTest1 {

    @Test
    public void test1() {
        Channel channel = null;
        ChannelFuture channelFuture = channel.connect(new InetSocketAddress("www.baidu.com", 80));
        channelFuture.addListener((ChannelFuture future) -> {
            if (future.isSuccess()) { // 成功处理

            } else { // 错误处理

            }
        });
        ChannelFuture writeFutue = channel.write(null);
        writeFutue.addListener((ChannelFuture future) -> {
            if (future.isSuccess()) {

            } else {

            }
        });
    }

}
