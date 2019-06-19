package com.sun.health.newwork.netty.embedded_channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 华硕 on 2019-06-18.
 */
public class SimpleEmbeddedChannelTest1 {

    @Test
    public void testFrameDecoder() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        assertTrue(embeddedChannel.writeInbound(input.retain()));
        assertTrue(embeddedChannel.finish());
        ByteBuf read = embeddedChannel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();
        read = embeddedChannel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();
        read = embeddedChannel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(embeddedChannel.readInbound());
        buf.release();
    }

    @Test
    public void testFrameDecode2() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        assertFalse(embeddedChannel.writeInbound(input.readBytes(2)));
        assertTrue(embeddedChannel.writeInbound(input.readBytes(7)));
        assertTrue(embeddedChannel.finish());
        ByteBuf read = embeddedChannel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();
        read = embeddedChannel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();
        read = embeddedChannel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();
        assertNull(embeddedChannel.readInbound());
        buf.release();
    }

    @Test
    public void testEncoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            buf.writeInt(i * -1);
        }
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new AbsIntegerEncoder());
        assertTrue(embeddedChannel.writeOutbound(buf));
        assertTrue(embeddedChannel.finish());
        for (int i = 1; i < 10; i++) {
            assertEquals((Integer) i, (Integer) embeddedChannel.readOutbound());
        }
        assertNull(embeddedChannel.readOutbound());
//        buf.release();
    }

}
