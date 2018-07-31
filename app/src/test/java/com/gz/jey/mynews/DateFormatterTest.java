package com.gz.jey.mynews;

import com.gz.jey.mynews.utils.DatesFormatter;

import junit.framework.Assert;

import org.junit.Test;


public class DateFormatterTest {

    /**
     * Test Parsing date with expected dd/MM/yy format from a long format including utc zone
     */
    @Test
    public void TestLongFormat(){
        String date = "2017-02-16'T'15:35:25+04.00";
        ResultFromTest(date);
    }

    /**
     * Test Parsing date with expected dd/MM/yy format from a short format
     */
    @Test
    public void TestShortFormat(){
        String date = "2017-02-16";
        ResultFromTest(date);
    }

    /**
     * @param date String (the testing date format)
     * Comparing expected and result from DateFormatter
     */
    private void ResultFromTest(String date) {
        String expected = DatesFormatter.create().getDateFormated(date);
        String actual = "16/02/17";
        Assert.assertEquals(expected, actual);
    }
}
