package com.sun.health.newwork.tianjin;

import org.junit.Test;

/**
 * Created by 华硕 on 2019-05-05.
 */
public class Test6 {

    @Test
    public void test6() {
        String s = "http://enorth.com/system/2019/03/14/1.html";
        String path = null;
        int i = s.indexOf("://");
        if(i != -1) {
            path = s.substring(i + 3);
        }
        path = path.substring(path.indexOf("/"));
        System.out.println(path);
    }
}
