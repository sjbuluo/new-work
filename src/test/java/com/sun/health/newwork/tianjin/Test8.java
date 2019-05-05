package com.sun.health.newwork.tianjin;

import org.junit.Test;

/**
 * Created by 华硕 on 2019-05-05.
 */
public class Test8 {

    @Test
    public void test8() {
        int a = 10;
        int b = 14;
        int result = -1;
        for (int i = 1; i < 1000; i++) {
            if(i % a == 0 && i % b == 0) {
                result = i;
                break;
            }
        }
        if(result == -1) {
            System.out.println("不存在小于1000的最小公倍数");
        } else {
            System.out.println(result);
        }
    }

}
