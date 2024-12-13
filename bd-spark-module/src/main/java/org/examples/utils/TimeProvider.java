package org.examples.utils;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface TimeProvider {

    String date_pattern = "yyyy-MM-dd-mm-ss-SSS";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(date_pattern);

    default String nowAsString() {
        return now().format(formatter);
    }

    default LocalDateTime now(){
        return LocalDateTime.now(Clock.systemUTC());
    }

}
