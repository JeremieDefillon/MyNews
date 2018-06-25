package com.gz.jey.mynews.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {

    private static SimpleDateFormat[] dateFormat = {
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"),
            new SimpleDateFormat("yyyy-MM-dd")
    };
    // ex date string 2018-04-25T05:00:13-04:00

    public static String getDateFormated(String dateunformatted){
        Date dd = null;
        String d = "";
        for(SimpleDateFormat df : dateFormat ){
            try {
                dd = df.parse(dateunformatted);
                d = DateFormat.getDateInstance(DateFormat.SHORT).format(dd);
                break;
            } catch (ParseException e) {
                d= "";
            }
        }
        return d;
    }
}
