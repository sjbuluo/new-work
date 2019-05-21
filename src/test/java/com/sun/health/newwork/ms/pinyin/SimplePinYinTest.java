package com.sun.health.newwork.ms.pinyin;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by 华硕 on 2019-05-21.
 *
 */
public class SimplePinYinTest {


    /**
     * GBK简单测试
     * GBK是以拼音顺序来给中文排序的（多音字可能不准确 需要自行考虑)
     * 从有符号到无符号的2进制 16进制 10进制的转换
     */
    @Test
    public void test1() {
        try {
            print2_10_16('啊');
            print2_10_16('阿');
            print2_10_16('中');
            print2_10_16('终');
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private String toGBK10(char c) throws UnsupportedEncodingException {
        byte[] bytes = charToGBKByteArr(c);
        return String.valueOf(Integer.valueOf(toGBK2(c), 2));
    }

    private String toGBK16(char c) throws UnsupportedEncodingException {
        byte[] bytes = charToGBKByteArr(c);
        return (Integer.toHexString(bytes[0] & 0xff) + Integer.toHexString(bytes[1] & 0xff)).toUpperCase();
    }

    private String toGBK2(char c) throws UnsupportedEncodingException {
        byte[] bytes = charToGBKByteArr(c);
        return Integer.toBinaryString(bytes[0] & 255) + Integer.toBinaryString(bytes[1] & 255);
    }

    private void print2_10_16(char c) throws UnsupportedEncodingException {
        System.out.println("2: " + toGBK2(c) + " | 10: " + toGBK10(c) + " | 16: " + toGBK16(c));
    }

    private byte[] charToGBKByteArr(char c) throws UnsupportedEncodingException {
        return String.valueOf(c).getBytes("GBK");
    }
}
