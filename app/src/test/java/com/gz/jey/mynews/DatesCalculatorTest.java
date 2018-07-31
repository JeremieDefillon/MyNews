package com.gz.jey.mynews;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static com.gz.jey.mynews.utils.DatesCalculator.ConvertRequestToCalendar;
import static com.gz.jey.mynews.utils.DatesCalculator.ConvertRequestToStandardDate;
import static com.gz.jey.mynews.utils.DatesCalculator.GetOneWeekAgo;
import static com.gz.jey.mynews.utils.DatesCalculator.IntDateFormat;
import static com.gz.jey.mynews.utils.DatesCalculator.RequestDateFormat;
import static com.gz.jey.mynews.utils.DatesCalculator.SetupCustomDateInt;
import static com.gz.jey.mynews.utils.DatesCalculator.StandardStringDateFormat;

public class DatesCalculatorTest {

    private String dateStr1, dateStr2;
    private String dateForm1, dateForm2;
    private int[] dateInt1, dateInt2;

    /**
     * the SetUp to Initialize expected values
     */
    @Before
    public void SetUp(){
        // Initial Date for Test
        dateStr1 = "01/01/2018";
        dateForm1 = "20180101";
        dateInt1 = new int[]{1,1,2018};
        dateStr2 = "31/12/2018";
        dateForm2 = "20181231";
        dateInt2 = new int[]{31,12,2018};
    }

    /**
     * Test the converting date format as dd/MM/yyyy
     */
    @Test
    public void TestDateStr() {
        Calendar cal1 = SetupCustomDateInt(dateInt1);
        String result1 = StandardStringDateFormat(cal1);
        Assert.assertEquals(dateStr1, result1);

        Calendar cal2 = SetupCustomDateInt(dateInt2);
        String result2 = StandardStringDateFormat(cal2);
        Assert.assertEquals(dateStr2, result2);
    }

    /**
     * Test the converting date format as yyyyMMdd
     */
    @Test
    public void TestDateReq() {
        Calendar cal1 = SetupCustomDateInt(dateInt1);
        String result1 = RequestDateFormat(cal1);
        Assert.assertEquals(dateForm1, result1);

        Calendar cal2 = SetupCustomDateInt(dateInt2);
        String result2 = RequestDateFormat(cal2);
        Assert.assertEquals(dateForm2, result2);
    }

    /**
     * Test the converting date format as int array [dd,MM,yyyy]
     */
    @Test
    public void TestDateInt() {
        Calendar cal1 = SetupCustomDateInt(dateInt1);
        int[] result1 = IntDateFormat(cal1);
        Assert.assertEquals(dateInt1[0], result1[0]);
        Assert.assertEquals(dateInt1[1], result1[1]);
        Assert.assertEquals(dateInt1[2], result1[2]);

        Calendar cal2 = SetupCustomDateInt(dateInt2);
        int[] result2 = IntDateFormat(cal2);
        Assert.assertEquals(dateInt2[0], result2[0]);
        Assert.assertEquals(dateInt2[1], result2[1]);
        Assert.assertEquals(dateInt2[2], result2[2]);
    }

    /**
     * Test the converting date format as Calendar  from req format
     */
    @Test
    public void TestDateConvertReqToCal() {
        Calendar cal1 = SetupCustomDateInt(dateInt1);
        Calendar result1 = ConvertRequestToCalendar(dateForm1);
        Assert.assertEquals(cal1, result1);

        Calendar cal2 = SetupCustomDateInt(dateInt2);
        Calendar result2 = ConvertRequestToCalendar(dateForm2);
        Assert.assertEquals(cal2, result2);
    }

    /**
     * Test the converting date format as dd/MM/yyyy from yyyyMMdd
     */
    @Test
    public void TestConvertReqToStandard() {
        String result1 = ConvertRequestToStandardDate(dateForm1);
        Assert.assertEquals(dateStr1, result1);

        String result2 = ConvertRequestToStandardDate(dateForm2);
        Assert.assertEquals(dateStr2, result2);
    }

    /**
     * Test the calculator one week before
     */
    @Test
    public void TestGetOneWeekAgo(){
        Calendar end1 = SetupCustomDateInt(dateInt1);
        Calendar begin1 = GetOneWeekAgo(end1);
        String result1 = StandardStringDateFormat(begin1);

        Assert.assertEquals("25/12/2017", result1);

        Calendar end2 = SetupCustomDateInt(dateInt2);
        Calendar begin2 = GetOneWeekAgo(end2);
        String result2 = StandardStringDateFormat(begin2);

        Assert.assertEquals("24/12/2018", result2);
    }


}
