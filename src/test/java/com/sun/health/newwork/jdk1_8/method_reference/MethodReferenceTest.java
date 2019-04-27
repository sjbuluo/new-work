package com.sun.health.newwork.jdk1_8.method_reference;

import org.junit.Test;

public class MethodReferenceTest {

    @Test
    public void test1() {
        CustomConvert<Integer, String> convert = (String s) -> {
            return Integer.valueOf(s);
        };
        System.out.println(convert.convert("123"));
    }

    @Test
    public void test2() {
        CustomConvert<Integer, String> convert = Integer::valueOf;
        System.out.println(convert.convert("123"));
    }

    @Test
    public void test3() {
        UserFactory userFactory = User::new;
        System.out.println(userFactory.createUser("ZZ"));
    }

    @Test
    public void test4() {
        UserFactory userFactory = (String name) -> {
            return new User(name);
        };
        System.out.println(userFactory.createUser("ZZ"));
    }

}
