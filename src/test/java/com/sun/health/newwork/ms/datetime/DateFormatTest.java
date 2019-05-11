package com.sun.health.newwork.ms.datetime;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 华硕 on 2019-05-09.
 */
public class DateFormatTest {

    @Test
    public void test1() {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2019-05-09 16:25:24");
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
