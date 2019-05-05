package com.sun.health.newwork.tianjin;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * Created by 华硕 on 2019-05-05.
 */
public class Test3 {

    @Test
    public void test3() {
        String s = "I am 10 years old.";
        String[] strArr = s.split(" ");
        for (int i = 0; i < strArr.length; i++) {
            String str = strArr[i];
            char c= str.charAt(0);
            strArr[i] = (c >= 'a' && c <= 'z' ? Character.toUpperCase(c) : c) + str.substring(1);
        }
        String result = StringUtils.join(strArr, " ");
        System.out.println(result);
    }
}
