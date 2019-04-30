package com.sun.health.newwork.ms;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 华硕 on 2019-04-30.
 */
public class StringTest {

    @Test
    public void test1() {
        System.out.println("" == "");
    }

    @Test
    public void test2() {
        System.out.println("a" == "a");
    }

    @Test
    public void test3() {
        String str1 = "a";
        String str2 = "a";

        System.out.println(str1.hashCode());
        System.out.println(str2.hashCode());
    }

    @Test
    public void test4() {
        System.out.println("0".equals(0));
    }

    @Test
    public void test5() {
        

    }

}
