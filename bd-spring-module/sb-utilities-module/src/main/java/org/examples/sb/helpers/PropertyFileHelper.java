package org.examples.sb.helpers;

import java.util.Properties;

public class PropertyFileHelper {

    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
            //String authority = properties.getProperty("AUTHORITY");
            //String scope = properties.getProperty("SCOPE");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }
}
