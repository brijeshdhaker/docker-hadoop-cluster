package org.examples.utils;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyFileReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyFileReader.class);

    private PropertyFileReader(){

    }

    public static Map<String, String> loadFromDefaultFS(String path, FileSystem fs) {
        Properties properties = new LinkedProperties();
        if(path != null && !path.isEmpty()){
            System.out.println("Reading Workflow Properties from file " + path);
            try(FSDataInputStream fdis = fs.open(new Path(path)) ){
                properties.load(fdis);
            } catch (IOException e) {
                System.out.println("Failed to Read Workflow Properties from file " + path);
            }
        }

        Map<String, String> allProperties = new LinkedHashMap<>();
        properties.forEach((key, value) -> allProperties.put(key.toString(), value.toString()));
        LOGGER.info("Read {} properties ", allProperties.size());

        allProperties.forEach((k, v) -> System.out.println("***** Property " + k + " set to ****** " + v));
        return allProperties;
    }

    public static Map<String, String> loadFromFile(String path){
        Properties properties = new LinkedProperties();
        if(path != null && !path.isEmpty()){
            System.out.println("Reading Workflow Properties from file " + path);

            URL url = ClassLoader.getSystemResource(path);
            File file = (url != null) ? new File(url.getFile()) : new File(path);

            try(InputStream fis = new FileInputStream(file)){
                properties.load(fis);
            } catch (IOException e) {
                System.out.println("Failed to Read Workflow Properties from file " + path);
            }
        }

        Map<String, String> allProperties = new LinkedHashMap<>();
        properties.forEach((key, value) -> allProperties.put(key.toString(), value.toString()));
        LOGGER.info("Read {} properties ", allProperties.size());

        allProperties.forEach((k, v) -> System.out.println("***** Property " + k + " set to ****** " + v));
        return allProperties;
    }
}
