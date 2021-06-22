package com.malaab.ya.action.actionyamalaab.admin.utils;

import com.orhanobut.logger.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class DateTimeUtils {

    public static String PATTERN_DATETIME_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static String PATTERN_DATE_DEFAULT = "dd/MM/yyyy";

    public static String PATTERN_DATE = "dd MMM yyyy";
    public static String PATTERN_DATE_1 = "dd MMMM yyyy";
    public static String PATTERN_DATE_2 = "yyyy-MM-dd";
    public static String PATTERN_DATE_3 = "EEEE, dd MMMM yyyy";
    public static String PATTERN_DATE_4 = "dd-MM-yyyy";

    public static String PATTERN_TIME = "hh:mm a";
    private static String PATTERN_TIME_1 = "HH:mm:ss";


    public static String getStandardCurrentDatetime() {
        String currentDateAndTime = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATETIME_DEFAULT, Locale.ENGLISH);
            currentDateAndTime = sdf.format(new Date());
        } catch (Exception e) {
            Logger.i("Error in parsing date");
        }

        return currentDateAndTime;
    }

    public static String getCurrentDatetime(String format) {
        String currentDateAndTime = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
            currentDateAndTime = sdf.format(new Date());
        } catch (Exception e) {
            Logger.i("Error in parsing date");
        }

        return currentDateAndTime;
    }


    public static String getDatetime(long date, String format) {
        if (date <= 0) {
            return "";
        }

        String datetime = "";

        try {
            datetime = new SimpleDateFormat(format, Locale.getDefault()).format(date);

        } catch (android.net.ParseException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
            Logger.i("Error in parsing date");
        }

        return datetime;
    }

    public static String getDatetime(long date, String format, Locale locale) {
        if (date <= 0) {
            return "";
        }

        String datetime = "";

        try {
            datetime = new SimpleDateFormat(format, locale).format(date);

        } catch (android.net.ParseException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
            Logger.i("Error in parsing date");
        }

        return datetime;
    }


    public static String changeDateFormat(String dateString, String currentFormat, String requiredFormat) {
        String result = "";
        if (StringUtils.isEmpty(dateString)) {
            return result;
        }

        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.ENGLISH);
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.ENGLISH);
        Date date = null;

        try {
            date = formatterOld.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            result = formatterNew.format(date);
        }

        return result;
    }

    public static String changeDateFormat(Date date, String format) {
        if (date == null)
            return "";

        String datetime = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            datetime = sdf.format(date);
        } catch (Exception e) {
            Logger.i("Error in parsing date");
        }

        return datetime;
    }


    public static String convertShortDayNameToFullName(String shortName) {
        switch (shortName.toLowerCase()) {
            case "sat":
                return "Saturday";

            case "sun":
                return "Sunday";

            case "mon":
                return "Monday";

            case "tue":
                return "Tuesday";

            case "wed":
                return "Wednesday";

            case "thu":
                return "Thursday";

            case "fri":
                return "Friday";

            default:
                return "";
        }
    }


    public static long getCurrentDatetime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    public static long getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, 0, 0, 0);
        return calendar.getTimeInMillis();
    }


    public static int getHour(long datetime) {
        if (datetime <= 0) {
            return 0;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(datetime));

        return calendar.get(Calendar.HOUR);
    }

    public static String getHourName(long datetime) {
        if (datetime <= 0) {
            return "";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(datetime));

        return String.valueOf(calendar.get(Calendar.HOUR)) + ":" + String.valueOf(calendar.get(Calendar.MINUTE));
    }


    public static int getDay(long datetime) {
        if (datetime <= 0) {
            return 0;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(datetime));

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDay(String datetime) {
        if (StringUtils.isEmpty(datetime)) {
            return 0;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDateTime(datetime));

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getDayName(long datetime) {
        if (datetime <= 0) {
            return "";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(datetime));

        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
    }

    public static String getDayName(String datetime) {
        if (StringUtils.isEmpty(datetime)) {
            return "";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDateTime(datetime));

        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
    }


    public static String getMonthName(long datetime) {
        if (datetime <= 0) {
            return "";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(datetime));

        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }

    public static String getMonthName(String datetime) {
        if (StringUtils.isEmpty(datetime)) {
            return "";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDateTime(datetime));

        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
    }


    public static long getMilliseconds(String datetime) {
        if (StringUtils.isEmpty(datetime)) {
            return 0;
        }

        StringBuilder sbDate = new StringBuilder();
        sbDate.append(datetime);

        Date dateDT = null;

        String rDate = (sbDate.toString().contains("T")) ? sbDate.toString().replace("T", " ") : sbDate.toString();

        try {
            dateDT = new SimpleDateFormat(PATTERN_DATETIME_DEFAULT, Locale.ENGLISH).parse(rDate);
            AppLogger.w(String.valueOf(dateDT.getTime()));
            return dateDT.getTime();

        } catch (android.net.ParseException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
            Logger.i("Error in parsing date");
        }

        return 0;
    }


    public static Date addHoursToDate(String dateStr, int hours) {
        Date datetime = DateTimeUtils.parseDateTime(dateStr);

        Calendar cal = new GregorianCalendar();
        cal.setTime(datetime);
        cal.add(Calendar.HOUR_OF_DAY, hours);

        return cal.getTime();
    }

    public static Date addDaysToDate(String dateStr, int days) {
        Date datetime = DateTimeUtils.parseDateTime(dateStr);

        Calendar cal = new GregorianCalendar();
        cal.setTime(datetime);
        cal.add(Calendar.DAY_OF_MONTH, days);

        return cal.getTime();
    }

    public static Date addDaysToDate(long datetime, int days) {
        Date dt = new Date(datetime);

        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        cal.add(Calendar.DAY_OF_MONTH, days);

        return cal.getTime();
    }

    public static Date setTimeInDate(Date date, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);

        date = calendar.getTime();
        return date;
    }


    public static boolean isDatesEqual(long date1, long date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(date1);
        cal2.setTimeInMillis(date2);

        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

        return sameDay;
    }

    public static boolean isDateAfterCurrentDate(long datetime) {
        if (datetime <= 0) {
            return false;
        }

        Date currDate = new Date();
        Date date = new Date(datetime);

        return date.after(currDate);
    }

    public static boolean isDateAfterCurrentDate(String datetime) {
        if (StringUtils.isEmpty(datetime)) {
            return false;
        }

        Date currDate = parseDateTime(getStandardCurrentDatetime());
        Date date = parseDateTime(datetime);

        return date.after(currDate);
    }


    public static int getDaysDifference(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null)
            return 0;

        return (int) ((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static long getDifferenceInMilliseconds(Date startDate, Date endDate) {
        return endDate.getTime() - startDate.getTime();

//        long secondsInMilli = 1000;
//        long minutesInMilli = secondsInMilli * 60;
//        long hoursInMilli = minutesInMilli * 60;
//        long daysInMilli = hoursInMilli * 24;
//
//        long elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;
//
//        long elapsedHours = different / hoursInMilli;
//        different = different % hoursInMilli;
//
//        long elapsedMinutes = different / minutesInMilli;
//        different = different % minutesInMilli;
//
//        long elapsedSeconds = different / secondsInMilli;
//
//        AppLogger.w("days " + elapsedDays);
//        AppLogger.w("hours " + elapsedHours);
//        AppLogger.w("minutes " + elapsedMinutes);
//        AppLogger.w("seconds " + elapsedSeconds);
    }

    public static Date parseDateTime(String date) {
        if (date == null) {
            return null;
        }

        StringBuilder sbDate = new StringBuilder();
        sbDate.append(date);
        Date dateDT = null;

        String rDate = (sbDate.toString().contains("T")) ? sbDate.toString().replace("T", " ") : sbDate.toString();

        try {
            dateDT = new SimpleDateFormat(PATTERN_DATETIME_DEFAULT, Locale.ENGLISH).parse(rDate);
        } catch (android.net.ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.i("Error in parsing date");
        }

        return dateDT;
    }

    public static String getTime12Hour(long date) {
        if (date <= 0) {
            return "";
        }

        String datetime = "";

        try {
            datetime = new SimpleDateFormat(PATTERN_TIME, Locale.getDefault()).format(date);

        } catch (android.net.ParseException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
            Logger.i("Error in parsing date");
        }

        return datetime;
    }


    public static long getCurrentWeekStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);

        //first day of week
        calendar.set(Calendar.DAY_OF_WEEK, 1);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Date date = calendar.getTime();
        return date.getTime();
    }

    public static long getCurrentWeekEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);

        //first day of week
        calendar.set(Calendar.DAY_OF_WEEK, 7);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Date date = calendar.getTime();
        return date.getTime();
    }


    public static long getWeekStartDate(int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);

        //first day of week
//        calendar.set(Calendar.DAY_OF_WEEK, 1);

        int currWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        currWeek += week;

        calendar.set(Calendar.WEEK_OF_YEAR, currWeek);

        //first day of week
        calendar.set(Calendar.DAY_OF_WEEK, 1);

        Date date = calendar.getTime();
        return date.getTime();
    }

    public static long getWeekEndDate(int week) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);

        //first day of week
//        calendar.set(Calendar.DAY_OF_WEEK, 1);

        int currWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        currWeek += week;

        calendar.set(Calendar.WEEK_OF_YEAR, currWeek);

        //first day of week
        calendar.set(Calendar.DAY_OF_WEEK, 7);

        Date date = calendar.getTime();
        return date.getTime();
    }

    public static boolean isDateBetweenDates(long date, long startDate, long endDate) {
        Date d = new Date(date);

        Date min = new Date(startDate);
        Date max = new Date(endDate);

        return d.after(min) && d.before(max);
    }
}
