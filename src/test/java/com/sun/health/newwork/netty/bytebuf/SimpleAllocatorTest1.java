package com.sun.health.newwork.netty.bytebuf;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.socket.oio.OioSocketChannel;
import org.junit.Test;



/**
 * Created by 华硕 on 2019-06-17.
 */
public class SimpleAllocatorTest1 {

    @Test
    public void test1() {
        Channel channel = new OioSocketChannel();
        ByteBufAllocator alloc = channel.alloc();
        ByteBuf byteBuf = alloc.heapBuffer();
    }

}
