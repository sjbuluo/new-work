package com.sun.health.newwork.tianjin;

import org.junit.Test;

/**
 * Created by 华硕 on 2019-05-05.
 */
public class Test7 {

    @Test
    public void test7() {
        int a = 123456789;
        int b = 4;
        StringBuilder sa = new StringBuilder(String.valueOf(a)).reverse();
        int num = sa.length() / b;
        int index = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for ( ; index < num; ) {
            stringBuilder.append(sa.substring(index * b, (++index) * b)).append(",");
        }
        stringBuilder.append(sa.substring(index * b));
        System.out.println(stringBuilder.reverse().toString());
    }

    @Test
    public void test7_2() {
        int a = 123456789;
        int b = 4;
        String sa = String.valueOf(a);
        int num = sa.length() / b;
        int last = sa.length() % b;
        int index = 0;
        StringBuilder stringBuilder = new StringBuilder(sa.substring(0, last)).append(",");
        for ( ; index < num; ) {
            stringBuilder.append(sa.substring(index * b + last, (++index) * b + last)).append(",");
        }
        System.out.println(stringBuilder.substring(0, stringBuilder.length() - 1).toString());
    }


}
