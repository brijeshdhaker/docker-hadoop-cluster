package org.examples.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListUtil {


    public static List<Integer> listFromIntegers(String property){
        if(StringUtils.isEmpty(property)){
            return Collections.emptyList();
        }

        return Stream.of(property.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

    }

    public static List<Long> listFromLongs(String property){
        if(StringUtils.isEmpty(property)){
            return Collections.emptyList();
        }
        return Stream.of(property.split(",")).map(String::trim).map(Long::parseLong).collect(Collectors.toList());

    }

    public static List<String> listFromStrings(String property){

        if(StringUtils.isEmpty(property)){
            return Collections.emptyList();
        }

        return Stream.of(property.split(",")).collect(Collectors.toList());

    }
}
