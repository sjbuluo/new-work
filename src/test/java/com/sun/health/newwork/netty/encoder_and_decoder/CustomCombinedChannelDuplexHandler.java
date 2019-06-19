package com.sun.health.newwork.netty.encoder_and_decoder;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * Created by 华硕 on 2019-06-18.
 */
public class CustomCombinedChannelDuplexHandler extends CombinedChannelDuplexHandler<ToIntegerDecoder, ShortToByteEncoder> {

    public CustomCombinedChannelDuplexHandler() {
        super(new ToIntegerDecoder(), new ShortToByteEncoder());
    }
}
