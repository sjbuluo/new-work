package com.sun.health.newwork.quartz;

import org.junit.Test;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.impl.calendar.AnnualCalendar;
import org.quartz.impl.calendar.WeeklyCalendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarTest {

    @Test
    public void test1() {
        AnnualCalendar cal = new AnnualCalendar();
        Calendar excludeCalendar = new GregorianCalendar();
//        WeeklyCalendar
        cal.setDayExcluded(excludeCalendar, true);
    }

}
