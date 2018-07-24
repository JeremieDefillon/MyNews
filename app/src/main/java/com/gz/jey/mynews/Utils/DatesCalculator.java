package com.gz.jey.mynews.Utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatesCalculator {

    private static SimpleDateFormat df;

    public static Calendar SetUpCustomDateFromString(String date){
        Log.d("DATE", "date : "+date);
        String[] cut = date.split("/");
        int[] dt = {Integer.parseInt(cut[2]), Integer.parseInt(cut[1]), Integer.parseInt(cut[0])};
        Calendar cal = SetUpCustomDateFromIntArray(dt);
        return cal;
    }

    public static Calendar SetUpCustomDateFromIntArray(int[] dt){
        // SET NOW TIME
        Calendar now = Calendar.getInstance();

        // SETTINGS the Year , Month & Day wanted
        now.set(Calendar.YEAR, dt[0]);
        // The Month setted from 0 for January so we get it off 1
        now.set(Calendar.MONTH, (dt[1]-1));
        now.set(Calendar.DATE, dt[2]);
        now.set(Calendar.HOUR_OF_DAY,0);
        now.set(Calendar.MINUTE,0);
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MILLISECOND, 0);
        return now;
    }

    public static Calendar[] StartingDates(){
        // SET NOW TIME
        Calendar now = Calendar.getInstance();

        // SET 1 WEEK AGO TIME
        Calendar weekAgo = Calendar.getInstance();
        weekAgo.add(Calendar.WEEK_OF_MONTH, -1);

        Calendar[] ds = {weekAgo, now};

        return ds;
    }

    public static String strDateFormat(Calendar cal){
        df = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        // use the SimpleDateFormat to convert Calendar as String Formatted
        String datestr = df.format(cal.getTime());
        return datestr;
    }

    public static String strDateForReq(Calendar cal){
        df = new SimpleDateFormat("yyyyMMdd", Locale.FRANCE);
        // use the SimpleDateFormat to convert Calendar as String Formatted
        String datestr = df.format(cal.getTime());
        return datestr;
    }



    public static String strDateFromStrReq(String date){
        String year = date.substring(0,4);
        String month = date.substring(4,6);
        String day = date.substring(6,8);
        String datestr = day+"/"+month+"/"+year;
        return datestr;
    }

    public static int[] intDateFormat(Calendar cal){
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        // the month start from 0 to 1 in int so we get it and add 1 to it
        month++;
        int year = cal.get(Calendar.YEAR);
        int[]dates = {year,month,day};
        return dates;
    }

    public static String strTimeFromInt(int[] time){
        int h = time[0];
        int m = time[1];

        Calendar t = Calendar.getInstance();
        t.set(Calendar.HOUR_OF_DAY, h);
        t.set(Calendar.MINUTE, m);

        df = new SimpleDateFormat("HH:mm", Locale.FRANCE);
        // use the SimpleDateFormat to convert Calendar as String Formatted
        String timestr = df.format(t.getTime());
        return timestr;
    }

}
