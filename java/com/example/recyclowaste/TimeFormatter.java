package com.example.recyclowaste;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeFormatter {
    private static String formattedTime;
    private static Calendar calendar = Calendar.getInstance();
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");

    public static String ampmTime (String time) {
        String[] timeArr = time.split(":");

        int HOUR = Integer.parseInt(timeArr[0]);
        int MINUTE = Integer.parseInt(timeArr[1]);

        calendar.set(Calendar.HOUR_OF_DAY, HOUR);
        calendar.set(Calendar.MINUTE, MINUTE);

        formattedTime = simpleDateFormat.format(calendar.getTime());

        if(HOUR < 12) {
            formattedTime = formattedTime + " AM";
        }else {
            formattedTime = formattedTime + " PM";
        }

        return formattedTime;
    }

    public static String ampmTime(Calendar calendar) {
        int HOUR = calendar.get(Calendar.HOUR_OF_DAY);
        int MINUTE = calendar.get(Calendar.MINUTE);

        formattedTime = simpleDateFormat.format(calendar.getTime());

        if(HOUR < 12) {
            formattedTime = formattedTime + " AM";
        }else {
            formattedTime = formattedTime + " PM";
        }
        return formattedTime;
    }
}
