package com.sun.health.newwork.netty.server.in_one;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 华硕 on 2019-06-19.
 */
public class SimpleHTTPServerInOne {

    @Test
    public void testServer() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(9362)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new HttpResponseEncoder())
//                                    .addLast(new MessageToMessageEncoder<Object>() {
//                                        @Override
//                                        protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List<Object> list) throws Exception {
//                                            if (o instanceof String) {
//                                                list.add(channelHandlerContext.alloc().buffer().writeCharSequence((String) o, CharsetUtil.UTF_8));
//                                            }
//                                            if (o instanceof ByteBuf) {
//                                                System.out.println("空的");
//                                                throw new IllegalStateException();
//                                            }
//                                        }
//                                    })
                                    .addLast(new MessageToByteEncoder<Object>() {
                                        @Override
                                        protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
                                            if (o instanceof String) {
                                                byteBuf.writeCharSequence((String) o, CharsetUtil.UTF_8);
                                            }
                                            if (o instanceof ByteBuf) {
                                                System.out.println("空的");
                                                throw new IllegalStateException();
                                            }
                                        }
                                    })
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            System.out.println(((ByteBuf)msg).toString(CharsetUtil.UTF_8));
                                            ctx.write(msg);
                                        }

                                        @Override
                                        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //                                    ctx.writeAndFlush(Unpooled.copiedBuffer("Welcome".getBytes())).addListener(ChannelFutureListener.CLOSE);
//                                            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer("Welcome!", CharsetUtil.UTF_8));
//                                            response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/explain; charset=UTF-8");
//                                            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                                            ctx.writeAndFlush("Welcome!").addListener(ChannelFutureListener.CLOSE);
                                        }

                                        @Override
                                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                            cause.printStackTrace();
                                            ctx.close();
                                        }
                                    });
                        }
                    });
            ChannelFuture bind = bootstrap.bind().sync();
//            TimeUnit.SECONDS.sleep(60);
            bind.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

}
