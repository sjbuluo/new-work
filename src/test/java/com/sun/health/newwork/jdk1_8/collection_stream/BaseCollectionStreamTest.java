package com.sun.health.newwork.jdk1_8.collection_stream;

import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BaseCollectionStreamTest {

    @Test
    public void testList() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(i);
        }
        System.out.println(list);
        List<Integer> newList = list.stream().filter(num -> {
            return num % 2 == 0;
        }).map(num -> {
            return num / 2;
        }).collect(Collectors.toList());
        System.out.println(list);
        System.out.println(newList);
    }

    @Test
    public void testMap() {
        Map<String, Object> map = new HashMap<>();
        map.entrySet().stream();
    }

    @Test
    public void testArray() {
        int[] arr = new int[50];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        System.out.println(Arrays.toString(arr));
        int[] newArr = Arrays.stream(arr).filter(num -> {
            return num % 2 != 0;
        }).map(num -> {
            return num;
        }).toArray();
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(newArr));
    }

    @Test
    public void testFile() {
        File file = new File("");
    }

    @Test
    public void testInfiniteStream() {
        Stream<Integer> iterate = Stream.iterate(0, (n) -> n);
        Optional<Integer> first = iterate.filter(n -> n % 2 == 1).findFirst();
        System.out.println(first.get());
    }

    @Test
    public void testReduce() {
        Stream<Integer> stream = Stream.iterate(0, n -> n);
        Integer reduce = stream.filter(n -> n < 10).reduce(0, (x, y) -> x + y);
        System.out.println(reduce);
    }

}
