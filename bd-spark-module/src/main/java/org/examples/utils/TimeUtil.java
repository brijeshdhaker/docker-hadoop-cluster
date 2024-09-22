package org.examples.utils;

public class TimeUtil {


    public static String diffInMinAndSec(long timeMillisStart, long timeMillisEnd){

        long milli = timeMillisEnd - timeMillisStart;
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;

        long elapsedMinutes = milli / minutesInMilli;
        long elapsedSeconds = (milli % minutesInMilli) / secondsInMilli;

        return String.format("%d millis", milli);
    }
}
