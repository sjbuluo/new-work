package com.sun.health.newwork.netty.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by 华硕 on 2019-06-19.
 */
public class SimpleServerTest1 {

    @Test
    public void test1() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        EventLoopGroup cGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group, cGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(9362))
                    .childHandler(new SimpleChannelInitializer1())
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture bind = b.bind();
            bind.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println("启动成功");
                }
            });
            Scanner scanner = new Scanner(System.in);
            while ("quit".equals(scanner.nextLine())) {
                ChannelFuture channelFuture = bind.channel().closeFuture();
                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        System.out.println("关闭成功");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    @Test
    public void test2() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("localhost", 9362))
                    .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                            System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
                        }
                    });
            ChannelFuture sync = b.connect().sync();
            Channel channel = sync.channel();
            channel.writeAndFlush(Unpooled.copiedBuffer("123".getBytes())).addListener(ChannelFutureListener.CLOSE).sync();
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

}
