package org.examples.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CopyUtil {

    public static Timestamp copy(Timestamp timestamp){
        return timestamp == null ? null : new Timestamp(timestamp.getTime());
    }

    public static byte[] copy(byte[] bytes){
        return bytes == null ? null : Arrays.copyOf(bytes, bytes.length);
    }

    public static <T> List<T> copy(List<T> list){
        return list == null ? null : new ArrayList<>(list);
    }
}
