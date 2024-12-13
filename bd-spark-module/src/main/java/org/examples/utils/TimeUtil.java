package org.examples.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {


    private static DateTimeFormatter log_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String toString(LocalDateTime time){
        return time.format(log_formatter);
    }

    public static String diffWithNow(LocalDateTime start){
        return diff(start, LocalDateTime.now());
    }

    public static String diff(LocalDateTime start, LocalDateTime end){

        long millis = Duration.between(start, end).toMillis();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = minutesInMilli * 24;

        long elapsedDays = millis / daysInMilli;
        millis = millis % daysInMilli;

        long elapsedHours = millis / hoursInMilli;
        millis = millis % hoursInMilli;

        long elapsedMinutes = millis / minutesInMilli;
        millis = millis % minutesInMilli;

        long elapsedSeconds = millis / secondsInMilli;

        return String.format("%d day(s), %d hour(s), %d minute(s), %d second(s)", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

    }

    public static String diffInMinAndSec(long timeMillisStart, long timeMillisEnd){

        long milli = timeMillisEnd - timeMillisStart;
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;

        long elapsedMinutes = milli / minutesInMilli;
        long elapsedSeconds = (milli % minutesInMilli) / secondsInMilli;

        return String.format("%d millis", milli);
    }
}
