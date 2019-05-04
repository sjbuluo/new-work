package com.sun.health.newwork.interview.java.string;

import org.junit.Test;

public class SimpleStringTest {


    @Test
    public void testIntern() {
        String str = "abc";
        String str1 = new String("abc");
        String str2 = new String("abc");
        System.out.println(str == str1);
        System.out.println(str == str2);
        System.out.println(str1 == str2);
        System.out.println(str.intern() == str1.intern());
        System.out.println(str.intern() == str2.intern());
        System.out.println(str1.intern() == str2.intern());
    }

}
