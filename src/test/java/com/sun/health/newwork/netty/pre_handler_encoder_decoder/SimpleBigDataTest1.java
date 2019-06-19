package com.sun.health.newwork.netty.pre_handler_encoder_decoder;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.socket.oio.OioSocketChannel;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by 华硕 on 2019-06-19.
 */
public class SimpleBigDataTest1 {

    @Test
    public void test1() {
        File file = new File("readme.txt");
        try (FileInputStream in = new FileInputStream(file)) {
            FileRegion fileRegion = new DefaultFileRegion(in.getChannel(), 0, file.length());
            new OioSocketChannel().writeAndFlush(fileRegion).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println("传输文件成功");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
