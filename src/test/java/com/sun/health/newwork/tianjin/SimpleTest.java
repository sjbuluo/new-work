package com.sun.health.newwork.tianjin;

import org.junit.Test;

/**
 * Created by 华硕 on 2019-05-05.
 */
public class SimpleTest {

    @Test
    public void test1() {
        String s = "ABCDE";
        StringBuilder stringBuilder = new StringBuilder(s);
        System.out.println(s + stringBuilder.reverse().toString());
    }

    @Test
    public void test2() {
        int a = 45;
        int b = 12;
        StringBuilder stringBuilder = new StringBuilder().append(a / 10).append(b / 10).append(a % 10).append(b % 10);
        System.out.println(stringBuilder);
    }


}
