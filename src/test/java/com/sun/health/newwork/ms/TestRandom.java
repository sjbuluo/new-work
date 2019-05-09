package com.sun.health.newwork.ms;

import org.junit.Test;

import java.util.HashSet;
import java.util.Random;

/**
 * Created by 华硕 on 2019-05-06.
 */
public class TestRandom {

    @Test
    public void test1() {
        HashSet<Integer> set = new HashSet<>();
        Random random = new Random();
        while(set.size() < 4) {
            set.add(random.nextInt(4));
        }
        System.out.println(set);
        Integer i1 = new Integer(1);
        System.out.println(i1.hashCode());
        Integer i2 = new Integer(2);
        System.out.println(i2.hashCode());
        Integer i3 = new Integer(3);
        System.out.println(i3.hashCode());
        Integer i4 = new Integer(4);
        System.out.println(i4.hashCode());
    }

}
