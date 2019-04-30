package com.sun.health.newwork.ms;

import org.junit.Test;
import sun.security.provider.MD5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by 华硕 on 2019-04-30.
 */
public class TestUUID {

    @Test
    public void test1() {
        UUID uuid = UUID.randomUUID();
        String s = uuid.toString();
        System.out.println(s);
        System.out.println(s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24));
        System.out.println(s.replace("-", ""));
    }

    @Test
    public void test2() throws NoSuchAlgorithmException {

    }

}
