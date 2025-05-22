package org.examples.sb.helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeHelper {

    public static final String YYYY_MM_DD_DELIMITED_DASH = "yyyy-MM-dd";

    public static final String MM_DD_YYYY_DELIMITED_DASH = "MM-dd-yyyy";

    public static final String MMM_DD_YYYY_DELIMITED_DASH = "MMM-dd-yyyy";

    /*
    public static LocalDateTime convertToLocalDateTime(String data, String pattern){
        retrun conve
    }
     */
    public static String convertToLocalDateString(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate().format(DateTimeFormatter.ofPattern(YYYY_MM_DD_DELIMITED_DASH)).toString();
    }

    public static LocalDate convertToLocalDate(String datetime, String pattern) {
        return LocalDate.parse(datetime, DateTimeFormatter.ofPattern(pattern));
    }


}
