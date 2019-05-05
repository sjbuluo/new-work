package com.sun.health.newwork.tianjin;

import org.junit.Test;

/**
 * Created by 华硕 on 2019-05-05.
 */
public class Test5 {

    @Test
    public void test5() {
        String s = "       Hello  everyone. Let's  help children   in need.                  ";
        s = s.replaceFirst("^\\s+", "");
        s = s.replaceFirst("\\s+$","");
        s = s.replaceAll("\\s+", " ");
        System.out.println(s);
        System.out.println(s.length());
    }

}
