package com.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Time에 관련된 Class
 */
public class TimeFormatter {
    /**
     * Time Constant
     */
    private static final int SEC = 60;
    private static final int MIN = 60;
    private static final int HOUR = 24;
    private static final int DAY = 30;
    private static final int MONTH = 12;

    /**
     * @return 현재 시간 FORM에 맞춰 리턴
     * "yyyy-MM-dd HH:mm:ss"
     * "yyyy년 MM월dd일 HH시mm분ss초"
     * "yyyy_MM_dd_HH_mm_ss"
     */
    public static String TimeFormatCurrent(String format) {
        String basicFormat = "yyyy-MM-dd HH:mm:ss";
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat(Objects.isNull(format) ? basicFormat : format);
        return dateFormat.format(now);
    }

    public static String TimeFormatLongToString(String format, long long_time) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.KOREA);
        return dateFormat.format(long_time);
    }

    public static String TimeFormatStringToString(String to_format, String from_format, String time) throws ParseException {
        DateFormat from_dateFormat = new SimpleDateFormat(to_format);
        Date target_date = from_dateFormat.parse(time);
        DateFormat to_dateFormat = new SimpleDateFormat(from_format, Locale.KOREA);
        return to_dateFormat.format(target_date);
    }

    /**
     * @return 현재 시간 + days day를 늘려서 Stamp로 리턴
     * 현재 Stamp에서 days day를 늘린 Stamp
     */
    public static Date LongTimeStamp(int days) {
        Date currentDate = new Date();
        // convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        // manipulate date
        c.add(Calendar.YEAR, 0);
        c.add(Calendar.MONTH, 0);
        c.add(Calendar.DATE, days); //same with c.add(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.HOUR, 0);
        c.add(Calendar.MINUTE, 0);
        c.add(Calendar.SECOND, 0);
        // convert calendar to date
        Date currentDatePlusOne = c.getTime();
        return currentDatePlusOne;
    }

    /**
     * @return 임의의 Date에 + i day를 늘려서 Stamp로 리턴
     * 임의의 Stamp에서 i day를 늘린 Stamp
     */
    public static Date LongTimeStamp(Date date, int i) {
        Date currentDate = date;
        // convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        // manipulate date
        c.add(Calendar.YEAR, 0);
        c.add(Calendar.MONTH, 0);
        c.add(Calendar.DATE, i); //same with c.add(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.HOUR, 0);
        c.add(Calendar.MINUTE, 0);
        c.add(Calendar.SECOND, 0);
        // convert calendar to date
        Date currentDatePlusOne = c.getTime();
        return currentDatePlusOne;
    }

    public static String TimeFormatChatTimeString(LocalDateTime time) throws ParseException {
        LocalDateTime now = LocalDateTime.now();
        long diffTime = time.until(now, ChronoUnit.SECONDS); // now보다 이후면 +, 전이면 -
        String msg = null;
        if (diffTime < SEC) {
            return diffTime + "초전";
        }
        diffTime = diffTime / SEC;
        if (diffTime < MIN) {
            return diffTime + "분 전";
        }
        diffTime = diffTime / MIN;
        if (diffTime < HOUR) {
            return diffTime + "시간 전";
        }
        diffTime = diffTime / HOUR;
        if (diffTime < DAY) {
            return diffTime + "일 전";
        }
        diffTime = diffTime / DAY;
        if (diffTime < MONTH) {
            return diffTime + "개월 전";
        }
        return TimeFormatLongToString("yyyy-MM-dd HH:mm:ss", diffTime);
    }

    public static String StringDateFormatDayOfWeek(String date) {
        Date time = null;
        try {
            time = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).parse(date);
        } catch (ParseException e) {
            try {
                time = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN).parse(date);
            } catch (ParseException parseException) {
                e.printStackTrace();
                parseException.printStackTrace();
            }
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        String korDayOfWeek = null;
        switch (dayOfWeek) {
            case 1:
                korDayOfWeek = "일";
                break;
            case 2:
                korDayOfWeek = "월";
                break;
            case 3:
                korDayOfWeek = "화";
                break;
            case 4:
                korDayOfWeek = "수";
                break;
            case 5:
                korDayOfWeek = "목";
                break;
            case 6:
                korDayOfWeek = "금";
                break;
            case 7:
                korDayOfWeek = "토";
                break;
        }
        return korDayOfWeek;
    }
}
