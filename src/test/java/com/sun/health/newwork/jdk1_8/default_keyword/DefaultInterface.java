package com.sun.health.newwork.jdk1_8.default_keyword;

public interface DefaultInterface {

    String hello();

    default String hello(String name) {
        return "Hello" + name;
    }

}
