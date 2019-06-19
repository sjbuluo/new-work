package com.sun.health.newwork.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;

import java.util.List;

/**
 * Created by 华硕 on 2019-06-19.
 */
public class SimpleChannelInitializer1 extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
//                .addLast(new HttpResponseEncoder())
                .addLast(new MessageToMessageEncoder<String>() {
                    @Override
                    protected void encode(ChannelHandlerContext channelHandlerContext, String o, List<Object> list) throws Exception {
                        list.add(Unpooled.copiedBuffer(o, CharsetUtil.UTF_8));
                    }
                })
                .addLast(new SimpleChannelInboundHandler1())
                ;
//                .addLast(new SimpleChannelOutboundHandler1());
//        .addLast(new HttpServerCodec())
//        .addLast(new HttpObjectAggregator(65536))
//        .addLast(new ChunkedWriteHandler())
//        .addLast(new ChannelInboundHandlerAdapter() {
//            @Override
//            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                if (msg instanceof FullHttpRequest) {
//                    FullHttpRequest req = (FullHttpRequest) msg;
//                    try {
//                        String uri = req.uri();
//                        ByteBuf contentBuf = req.content();
//                        String content = contentBuf.toString(CharsetUtil.UTF_8);
//                        HttpMethod method = req.method();
//                        HttpHeaders headers = req.headers();
//                        response(ctx);
//                    } finally {
//                        req.release();
//                    }
//                } else {
//                    super.channelRead(ctx, msg);
//                }
//            }
//
//            private void response(ChannelHandlerContext ctx) {
//                FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer("Welcome!", CharsetUtil.UTF_8));
//                resp.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/explain; charset=UTF-8");
//                ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
//            }
//        });
    }
}
