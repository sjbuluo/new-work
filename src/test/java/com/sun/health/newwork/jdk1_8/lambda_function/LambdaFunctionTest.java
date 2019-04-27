package com.sun.health.newwork.jdk1_8.lambda_function;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;

public class LambdaFunctionTest {

    @Test
    public void test1() {
        ArrayList<Integer> intList1 = new ArrayList<>();
        ArrayList<Integer> intList2 = new ArrayList<>();
        ArrayList<Integer> intList3 = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            intList1.add(random.nextInt(100));
            intList2.add(random.nextInt(100));
            intList3.add(random.nextInt(100));
        }
        System.out.println(intList1);
        Collections.sort(intList1, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        System.out.println(intList1);

        System.out.println(intList2);
        Collections.sort(intList2, (Comparator<? super Integer>) (o1, o2) -> {
            return o1 - o2;
        });
        System.out.println(intList2);

        System.out.println(intList3);
        Collections.sort(intList3, (o1, o2) -> {
            return o1 - o2;
        });
        System.out.println(intList3);
    }

}
