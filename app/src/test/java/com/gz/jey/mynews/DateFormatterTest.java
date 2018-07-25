package com.gz.jey.mynews;

import junit.framework.Assert;

import org.junit.Test;

import static com.gz.jey.mynews.utils.DatesFormatter.getDateFormated;

public class DateFormatterTest {

    String actual = "16/02/17";

    @Test
    public void TestLongFormat(){
        String date = "2017-02-16'T'15:35:25+04.00";
        ResultFromTest(date);
    }


    @Test
    public void TestShortFormat(){
        String date = "2017-02-16";
        ResultFromTest(date);
    }

    private void ResultFromTest(String date) {
        String expected = getDateFormated(date);
        Assert.assertEquals(expected, actual);
    }
}
