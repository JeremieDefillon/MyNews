package com.gz.jey.mynews.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatesFormatter {

    public DatesFormatter(){ }

    public static DatesFormatter create(){
        return (new DatesFormatter());
    }
    private SimpleDateFormat[] dateFormat = {
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'XXX'", Locale.FRANCE),
            new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE)
    };
    // ex date string 2018-04-25T05:00:13-04:00

    public String getDateFormated(String dateUnformatted){
        Date dd;
        String d = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.FRANCE);
        for(SimpleDateFormat df : dateFormat ){
            try {
                dd = df.parse(dateUnformatted);
                d = sdf.format(dd);
                break;
            } catch (ParseException e) {
                d= "";
            }
        }
        return d;
    }
}
