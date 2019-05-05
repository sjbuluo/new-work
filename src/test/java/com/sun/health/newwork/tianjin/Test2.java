package com.sun.health.newwork.tianjin;

import org.junit.Test;

/**
 * Created by 华硕 on 2019-05-05.
 */
public class Test2 {

    @Test
    public void test2() {
        int a = 45;
        int b = 12;
        StringBuilder stringBuilder = new StringBuilder().append(a / 10).append(b / 10).append(a % 10).append(b % 10);
        int c = Integer.valueOf(stringBuilder.toString());
        System.out.println(c);
    }

}
