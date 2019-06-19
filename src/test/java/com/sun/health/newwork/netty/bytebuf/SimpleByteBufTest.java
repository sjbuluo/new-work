package com.sun.health.newwork.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Created by 华硕 on 2019-06-17.
 */
public class SimpleByteBufTest {

    @Test
    public void test1() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in action rocks!.", utf8);
        System.out.println(buf.toString(utf8));
        ByteBuf slice = buf.slice(0, 15);
        System.out.println(slice.toString(utf8));
        buf.setByte(0, 96);
        System.out.println(slice.toString(utf8));
    }

    @Test
    public void test2() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in action rocks!.", utf8);
        System.out.println(buf.toString(utf8));
        ByteBuf slice = buf.copy(0, 15);
        System.out.println(slice.toString(utf8));
        buf.setByte(0, 96);
        System.out.println(slice.toString(utf8));
        System.out.println(buf.toString(utf8));
    }

    private ByteBuf byteBuf = null;
    private Charset charset = null;

    @Before
    public void before() {
        charset = Charset.forName("UTF-8");
        byteBuf = Unpooled.copiedBuffer("Netty in action rocks!", charset);
    }

    @Test
    public void test3() {
        printByteBuf();
        System.out.println(Character.valueOf((char) byteBuf.getByte(0)));
        printByteBuf();
        byteBuf.setByte(0, 'M');
        printByteBuf();
    }

    @Test
    public void test4() {
        printByteBuf();
        System.out.println(Character.valueOf((char) byteBuf.readByte()));
        printByteBuf();
        byteBuf.writeByte('M');
        System.out.println(byteBuf.writerIndex());
        printByteBuf();
    }

    private void printByteBuf() {
        System.out.println(byteBuf.toString(charset));
        System.out.println(byteBuf.readerIndex());
        System.out.println(byteBuf.writerIndex());
    }

}
