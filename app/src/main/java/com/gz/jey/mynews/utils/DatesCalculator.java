package com.gz.jey.mynews.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatesCalculator {

    private static SimpleDateFormat df;



    public static Calendar GetOneWeekAgo(Calendar date){
        // SET 1 WEEK AGO TIME
        date.add(Calendar.WEEK_OF_MONTH, -1);
        return date;
    }


    public static Calendar SetupCustomDateInt(int[] dt){
        // SET NOW TIME
        Calendar now = Calendar.getInstance();
        // SETTINGS the Year , Month & Day wanted
        now.set(Calendar.DAY_OF_MONTH, dt[0]);
        // The Month setted from 0 for January so we get it off 1
        now.set(Calendar.MONTH, dt[1]-1);
        now.set(Calendar.YEAR, dt[2]);
        now.set(Calendar.HOUR_OF_DAY,0);
        now.set(Calendar.MINUTE,0);
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MILLISECOND, 0);
        return now;
    }

    public static String StandardStringDateFormat(Calendar cal){
        df = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        // use the SimpleDateFormat to convert Calendar as String Formatted
        return df.format(cal.getTime());
    }

    public static String RequestDateFormat(Calendar cal){
        df = new SimpleDateFormat("yyyyMMdd", Locale.FRANCE);
        // use the SimpleDateFormat to convert Calendar as String Formatted
        return df.format(cal.getTime());
    }

    public static int[] IntDateFormat(Calendar cal){
        int d = cal.get(Calendar.DAY_OF_MONTH);
        int m = cal.get(Calendar.MONTH)+1;
        int y = cal.get(Calendar.YEAR);

        return new int[]{d,m,y};
    }

    public static Calendar ConvertRequestToCalendar(String dd){
        int d = Integer.parseInt(dd.substring(6,8));
        int m = Integer.parseInt(dd.substring(4,6));
        int y = Integer.parseInt(dd.substring(0,4));

        int[] dateInts = {d,m,y};
        return SetupCustomDateInt(dateInts);
    }

    public static String ConvertRequestToStandardDate(String dd){
        String d = dd.substring(6,8);
        String m = dd.substring(4,6);
        String y = dd.substring(0,4);

        return d+"/"+m+"/"+y;
    }


    public static String StandardStringTimeFormat(int[] time){
        int h = time[0];
        int m = time[1];

        Calendar t = Calendar.getInstance();
        t.set(Calendar.HOUR_OF_DAY, h);
        t.set(Calendar.MINUTE, m);

        df = new SimpleDateFormat("HH:mm", Locale.FRANCE);
        // use the SimpleDateFormat to convert Calendar as String Formatted
        return df.format(t.getTime());
    }

}
