package com.sun.health.newwork.netty.transform;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by 华硕 on 2019-06-15.
 */
public class OIOWithNetty {

    @Test
    public void testServer() throws InterruptedException {

        ChannelInboundHandlerAdapter handler = new ChannelInboundHandlerAdapter() {

            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                ctx.writeAndFlush(Unpooled.copiedBuffer("Hi!\r\n", CharsetUtil.UTF_8)).addListener(ChannelFutureListener.CLOSE);
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                cause.printStackTrace();
                ctx.close();
            }
        };
        EventLoopGroup group = new OioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group)
                    .channel(OioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(9362))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(handler);
                        }
                    });
            ChannelFuture bind = serverBootstrap.bind().sync();
            bind.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();
        }

    }

    @Test
    public void testClient() throws InterruptedException {
        SimpleChannelInboundHandler<ByteBuf> handler = new SimpleChannelInboundHandler<ByteBuf>() {
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf b) throws Exception {
                System.out.println(b.toString(CharsetUtil.UTF_8));
            }
        };
        EventLoopGroup group = new OioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(OioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("localhost", 9362))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(handler);
                        }
                    });
            ChannelFuture connect = bootstrap.connect().sync();
            TimeUnit.SECONDS.sleep(1);
            connect.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

}
