package com.sun.health.newwork.jdk1_8.fucntional_interface;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class CustomFunctionalInterfaceTest {

    private void printRandomString(CustomFunctionalInterface customFuncationalInterface) {
        System.out.println(customFuncationalInterface.randomString());
    }

    @Test
    public void test1() {
        CustomFunctionalInterfaceTest customFunctionalInterfaceTest = new CustomFunctionalInterfaceTest();
        customFunctionalInterfaceTest.printRandomString((CustomFunctionalInterface) () -> {
            Random random = new Random();
            byte[] bytes = new byte[10];
//            for (int i = 0; i < bytes.length; i++) {
//                bytes[i] = random.nextBytes(bytes);
//                System.out.println(bytes[i]);
//            }
            random.nextBytes(bytes);
//            try {
                return new String(bytes);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//                return "error";
//            }
        });
    }

    @Test
    public void test2() {
        CustomFunctionalInterfaceTest customFunctionalInterfaceTest = new CustomFunctionalInterfaceTest();
        customFunctionalInterfaceTest.printRandomString(() -> {
            return "123";
        });
    }

}
