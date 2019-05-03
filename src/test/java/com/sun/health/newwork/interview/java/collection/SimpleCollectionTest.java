package com.sun.health.newwork.interview.java.collection;

import org.junit.Test;

import java.util.*;

public class SimpleCollectionTest {

    class SameHashCode {

        private int id;

        public SameHashCode(int id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public String toString() {
            return "SameHashCode{" +
                    "id=" + id +
                    '}';
        }
    }

    @Test
    public void testCollectionSort() {
        List<Integer> list = Arrays.asList(2, 3, 1);
        System.out.println(list);
        Collections.sort(list, (int1, int2) -> {
            return int1 - int2;
        });
        System.out.println(list);
    }

    @Test
    public void testRemove() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.add(4);
        arrayList.add(5);
        arrayList.add(6);
        Integer remove = arrayList.remove(3);
    }

    @Test
    public void testHashSet() {
        Set<SameHashCode> set = new HashSet<>();
        set.add(new SameHashCode(1));
        set.add(new SameHashCode(2));
        set.add(new SameHashCode(3));
        set.add(new SameHashCode(4));
        System.out.println(set);
    }


    @Test
    public void testList() {
        new ArrayList<>();
    }

}
