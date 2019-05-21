package com.sun.health.newwork.interview.java.network;

import org.junit.Test;

import java.net.InetAddress;

/**
 * Created by 华硕 on 2019-05-21.
 */
public class SpamChecker {

    public static final String BLACKHOLE = "sbl.spamhaus.org";

    @Test
    public void test1() {
        String[] args = new String[] {
            "207.34.56.23",
            "125.12.32.4",
            "130.130.130.130",
        };
        for (String arg : args) {
            if(isSpammer(arg)) {
                System.out.println(arg + " 是垃圾邮件发送者");
            } else {
                System.out.println(arg + " 不是");
            }
        }
    }

    private boolean isSpammer(String arg) {
        try {
            InetAddress address = InetAddress.getByName(arg);
            byte[] quad = address.getAddress();
            String query = BLACKHOLE;
            for (byte octet : quad) {
                int unsignedByte = octet < 0 ? octet + 256 : octet;
                query = unsignedByte + "." + query;
            }
            InetAddress.getByName(query);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
