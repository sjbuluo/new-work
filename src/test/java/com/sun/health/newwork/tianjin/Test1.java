package com.sun.health.newwork.tianjin;

import org.junit.Test;

/**
 * Created by 华硕 on 2019-05-05.
 */
public class Test1 {

    @Test
    public void test1() {
        String s = "ABCDE";
        StringBuilder stringBuilder = new StringBuilder(s);
        System.out.println(s + stringBuilder.reverse().toString());
    }
}
