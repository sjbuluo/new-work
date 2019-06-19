package com.sun.health.newwork.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 华硕 on 2019-06-19.
 */
@ChannelHandler.Sharable
public class SimpleChannelInboundHandler1 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            System.out.println(buf.toString(CharsetUtil.UTF_8));
//            ctx.channel().writeAndFlush("Welcome!").addListener(ChannelFutureListener.CLOSE);
//            ctx.channel().writeAndFlush("Welcome!").addListener(ChannelFutureListener.CLOSE);
        }
//        if (ReferenceCountUtil.refCnt(msg) > 0) {
//            ReferenceCountUtil.release(msg);
//        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ByteBuf buf = ctx.alloc().buffer();
//        buf.writeCharSequence("Welcome!", CharsetUtil.UTF_8);
        System.out.println(123);
        ctx.writeAndFlush("Welcome!").addListener(ChannelFutureListener.CLOSE);
////        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer("Welcome!", CharsetUtil.UTF_8));
////        resp.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/explain; charset=UTF-8");
////        ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
    }
}
