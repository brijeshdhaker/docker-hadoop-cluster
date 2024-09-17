package org.examples.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiConsumer;

public class LinkedProperties extends Properties {

    private static final long serialVersionUID = -1L;
    private static final Logger log = LoggerFactory.getLogger(LinkedProperties.class);
    private Map<Object, Object> linkedHashMap = new LinkedHashMap<>();

    @Override
    public synchronized Object put(Object key, Object value) {
        return linkedHashMap.put(key, value);
    }

    @Override
    public boolean containsValue(Object value) {
        return linkedHashMap.containsValue(value);
    }

    @Override
    public synchronized boolean contains(Object value) {
        return linkedHashMap.containsValue(value);
    }

    @Override
    public Enumeration<Object> elements() {
        return super.elements();
    }

    @Override
    public Set<Object> keySet() {
        return linkedHashMap.keySet();
    }

    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
        return linkedHashMap.entrySet();
    }

    @Override
    public synchronized void forEach(BiConsumer<? super Object, ? super Object> action) {
        linkedHashMap.forEach(action);
    }

    @Override
    public int size() {
        return linkedHashMap.size();
    }

    @Override
    public String getProperty(String key) {
        return linkedHashMap.containsKey(key) ? linkedHashMap.get(key).toString() : null;
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return linkedHashMap.containsKey(key) ? linkedHashMap.get(key).toString() : defaultValue;
    }

    @Override
    public synchronized String toString() {
        return linkedHashMap.toString();
    }
}
