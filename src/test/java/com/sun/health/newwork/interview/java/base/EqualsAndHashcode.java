package com.sun.health.newwork.interview.java.base;

import org.junit.Test;

public class EqualsAndHashcode {

    class A {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof A;
        }
    }

    class B extends A {
        @Override
        public boolean equals(Object obj) {
            return obj instanceof B;
        }
    }

    @Test
    public void testEquals() {
        A a = new A();
        B b = new B();
        System.out.println(a.equals(b));
        System.out.println(b.equals(a));
    }

}
