package com.sun.health.newwork.ms;

import org.junit.Test;

/**
 * Created by 华硕 on 2019-04-30.
 */
public class TestInteger {

    @Test
    public void test1() {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(((2 << 31) - 1));
        System.out.println(((1 << 31)) - 1);
        System.out.println(Math.pow(2, 31) - 1);
        System.out.println(2 << 5);
        System.out.println(Long.MAX_VALUE);
    }

}
