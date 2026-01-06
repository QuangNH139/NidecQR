package com.nidec.qrattendance.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    
    public static String formatDateTime(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_DISPLAY, Locale.getDefault());
        return sdf.format(date);
    }
    
    public static String formatDateTimeForFile(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_FILE, Locale.getDefault());
        return sdf.format(date);
    }
    
    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
    
    public static Date getStartOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return getStartOfDay(calendar.getTime());
    }
    
    public static Date getStartOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getStartOfDay(calendar.getTime());
    }
    
    public static Date getTodayStart() {
        return getStartOfDay(new Date());
    }
    
    public static Date getTodayEnd() {
        return getEndOfDay(new Date());
    }
}
