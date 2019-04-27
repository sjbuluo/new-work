package com.sun.health.newwork.jdk1_8.default_keyword;

import org.junit.Test;

public class DefaultExtendsClass implements DefaultInterface {
    @Override
    public String hello() {
        return hello("World");
    }
}
