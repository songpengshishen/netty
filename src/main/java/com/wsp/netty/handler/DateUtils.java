package com.wsp.netty.handler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String getNowDateTimeStr(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return sdf.format(date);
    }
}
