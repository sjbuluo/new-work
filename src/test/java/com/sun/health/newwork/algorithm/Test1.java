package com.sun.health.newwork.algorithm;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 华硕 on 2019-04-30.
 */
public class Test1 {

    /**
     * 测试 x + y = x | y; 给定x，算出指定的第k个y
     */
    @Test
    public void test1() {
        for (int i = 1; i < 10; i++) {
            long x = test_f1(5, i);
            System.out.print(x);
            System.out.print(" -> ");
            System.out.println(x + 5 == (x | 5));
        }
    }

    private long test_f1(int x, int k) {
        char[] chars = Integer.toBinaryString(x).toCharArray();
        List<Integer> zeroIndexes = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            if(chars[i] == '0') {
                zeroIndexes.add(i);
            }
        }
        char[] kChars;
        String before = Integer.toBinaryString(k);
        if(before.length() > zeroIndexes.size()) {
            int beginIndex = before.length() - zeroIndexes.size();
            kChars = before.substring(beginIndex).toCharArray();
            before = before.substring(0, beginIndex);
        } else {
            kChars = before.toCharArray();
            before = "";
        }
        char[] tmpChars = new char[chars.length];
        Arrays.fill(tmpChars, '0');
        for (int i = kChars.length - 1; i >= 0; i--) {
            tmpChars[zeroIndexes.get(zeroIndexes.size() - (kChars.length - i))] = kChars[i];
        }
        return Integer.parseInt(before + String.valueOf(tmpChars), 2);
    }

}
