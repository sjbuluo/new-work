package com.sun.health.newwork.tianjin;

import org.junit.Test;

/**
 * Created by 华硕 on 2019-05-05.
 */
public class Test4 {

    @Test
    public void test4() {
        int n = 1000;
        int i1 = 1;
        int i2 = 1;
        int next = 1;
        while(next < n) {
            next = i1 + i2;
            i1 = i2;
            i2 = next;
        }
        System.out.println(i1);
    }

}
