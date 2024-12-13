package org.examples.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

public class JsonUtil {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJson(Object object){
        return ThrowWrappers.call(() -> mapper.writeValueAsString(object), "Json serialization failure");
    }

    public static byte[] toJsonBytes(Object object){
        return toJson(object).getBytes(StandardCharsets.UTF_8);
    }

    public static <T> T fromJson(byte[] src, Class<T> classz){
        return ThrowWrappers.call(() -> mapper.readValue(src, classz), "Json serialization failure");
    }

    public static <T> T fromJson(String json, Class<T> classz){
        return ThrowWrappers.call(() -> mapper.readValue(json, classz), "Json serialization failure");
    }

}
