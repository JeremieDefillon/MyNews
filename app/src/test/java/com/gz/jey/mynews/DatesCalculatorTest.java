package com.gz.jey.mynews;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static com.gz.jey.mynews.Utils.DatesCalculator.SetUpCustomDateFromIntArray;
import static com.gz.jey.mynews.Utils.DatesCalculator.SetUpCustomDateFromString;
import static com.gz.jey.mynews.Utils.DatesCalculator.StartingDates;
import static com.gz.jey.mynews.Utils.DatesCalculator.intDateFormat;
import static com.gz.jey.mynews.Utils.DatesCalculator.strDateFormat;

public class DatesCalculatorTest {

    String dateTest;
    int[] dateInt;

    @Before
    public void SetUp(){
        // Initial Date for Test
        dateTest = "17/11/1997";
        dateInt = new int[3];
        dateInt[0] = 1997;
        dateInt[1] = 11;
        dateInt[2] = 17;
    }

    @Test
    public void TestCustomDate(){

        // Convert string date into calendar
        Calendar date = SetUpCustomDateFromString(dateTest);

        // Convert same calendar into int array
        int[] dtint = intDateFormat(date);
        // Testing matches year, month and day returned as Int
        Assert.assertEquals(dateInt[0] , dtint[0]);
        Assert.assertEquals(dateInt[1] , dtint[1]);
        Assert.assertEquals(dateInt[2] , dtint[2]);

        // Convert same int array calendar into calendar
        Calendar dTest = SetUpCustomDateFromIntArray(dtint);
        // Testing matchs calendar from string & calendar from int array
        Assert.assertEquals(date, dTest);

        // Testing Both Calendars as string format "dd/MM/yyyy"
        String testDate = strDateFormat(date);
        Assert.assertEquals(dateTest, testDate);

        String testDTest = strDateFormat(dTest);
        Assert.assertEquals(dateTest, testDTest);
    }

    @Test
    public void TestStartingDates(){
        Calendar endDate = Calendar.getInstance();
        Calendar beginDate = endDate;

        beginDate.add(Calendar.WEEK_OF_MONTH,-1);

        Calendar[] beginAndEnd = StartingDates();
        // Testing Begin Date & End Date
        Assert.assertEquals(beginDate, beginAndEnd[0]);
        Assert.assertEquals(endDate, beginAndEnd[1]);
    }


}
