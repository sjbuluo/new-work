package com.sun.health.newwork.netty.pre_handler_encoder_decoder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * Created by 华硕 on 2019-06-18.
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {

    private final SslContext context;
    private final boolean startTls;

    public SslChannelInitializer(SslContext context, boolean startTls) {
        this.context = context;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        SSLEngine sslEngine = context.newEngine(channel.alloc());
        channel.pipeline().addFirst("ssl", new SslHandler(sslEngine, startTls));
    }
}
