package com.sun.health.newwork.jdk1_8.default_keyword;

import org.junit.Test;

public class DefaultKeyWordTest {

    @Test
    public void test() {
        DefaultExtendsClass defaultExtendsClass = new DefaultExtendsClass();
        System.out.println(defaultExtendsClass.hello());
        System.out.println(defaultExtendsClass.hello("ZZ"));
    }

}
