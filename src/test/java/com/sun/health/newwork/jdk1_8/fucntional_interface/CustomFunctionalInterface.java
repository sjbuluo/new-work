package com.sun.health.newwork.jdk1_8.fucntional_interface;

import java.util.Random;

@FunctionalInterface
public interface CustomFunctionalInterface {
    String randomString();

    default int randomInt() {
        return new Random().nextInt(100);
    }
}
