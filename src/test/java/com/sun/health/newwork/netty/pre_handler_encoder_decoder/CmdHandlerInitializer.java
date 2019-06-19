package com.sun.health.newwork.netty.pre_handler_encoder_decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * Created by 华硕 on 2019-06-18.
 */
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {

    public static final byte SPACE = (byte)' ';

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new CmdDecorder(64 * 1024));
        pipeline.addLast(new CmdHandler());
    }

    public static final class Cmd {
        private final ByteBuf name;
        private final ByteBuf option;

        public Cmd(ByteBuf name, ByteBuf option) {
            this.name = name;
            this.option = option;
        }

        public ByteBuf getName() {
            return name;
        }

        public ByteBuf getOption() {
            return option;
        }
    }

    public class CmdDecorder extends LineBasedFrameDecoder {

        public CmdDecorder(int maxLength) {
            super(maxLength);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
            ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);
            if (frame == null)
                return null;
            int index = frame.indexOf(frame.readerIndex(), frame.writerIndex(), SPACE);
            return new Cmd(frame.slice(frame.readerIndex(), index), frame.slice(index + 1, frame.writerIndex()));
        }
    }

    public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd> {

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Cmd cmd) throws Exception {
            // 处理命令
        }
    }
}
