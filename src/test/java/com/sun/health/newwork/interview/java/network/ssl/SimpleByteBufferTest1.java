package com.sun.health.newwork.interview.java.network.ssl;

import org.bson.ByteBuf;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by 华硕 on 2019-06-14.
 */
public class SimpleByteBufferTest1 {

    @Test
    public void test1() {
        int[] is = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
        System.out.println(Arrays.toString(is));
        System.arraycopy(is, 4, is, 0, 3);
        System.out.println(Arrays.toString(is));


        ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        System.out.println(byteBuffer);
        byteBuffer.put((byte) 'a');
        System.out.println(byteBuffer);
        byteBuffer.flip();
        System.out.println(byteBuffer);
        byteBuffer.compact();
        System.out.println(byteBuffer);
        byteBuffer.clear();
        System.out.println(byteBuffer);

        byteBuffer = ByteBuffer.wrap("Hello World".getBytes());
        System.out.println(byteBuffer);
        byteBuffer.flip();
        System.out.println(byteBuffer);
        byteBuffer.compact();
        System.out.println(byteBuffer);
        byteBuffer.clear();
        System.out.println(byteBuffer);
        System.out.println(byteBuffer.get());
        System.out.println(byteBuffer.get());
        byteBuffer.compact();
        System.out.println(byteBuffer);
        byteBuffer.put((byte) 'A');
        System.out.println(byteBuffer);
        byteBuffer.put((byte) 'a');
        System.out.println(byteBuffer);
        byteBuffer.flip();
        System.out.println(byteBuffer);
    }

}
