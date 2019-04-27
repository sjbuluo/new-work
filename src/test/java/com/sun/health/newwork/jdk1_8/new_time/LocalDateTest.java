package com.sun.health.newwork.jdk1_8.new_time;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.Date;

public class LocalDateTest {

    @Test
    public void test1() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        LocalDateTime of = LocalDateTime.of(2019, 01, 01, 01, 01, 01, 01);
        System.out.println(of);
        LocalDateTime parse = LocalDateTime.parse("2019-02-02T02:02:02");
        System.out.println(parse);
        int year = parse.get(ChronoField.YEAR);
        System.out.println(year);
        LocalDateTime with = parse.with(ChronoField.YEAR, 2020);
        System.out.println(with);
        LocalDateTime plus = with.plus(1, ChronoUnit.YEARS);
        System.out.println(plus);
    }

}
