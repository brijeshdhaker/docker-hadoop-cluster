package org.examples.flink.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.core.fs.FSDataInputStream;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class PropertyFileReader {

    public static Map<String, String> loadFromDefaultFS(String path) throws IOException {
        //
        FileSystem fileSystem = FileSystem.get(URI.create(path));
        log.info("FileSystem : HomeDirectory {}", fileSystem.getHomeDirectory().toString());
        log.info("FileSystem : WorkingDirectory {}", fileSystem.getWorkingDirectory().toString());
        log.info("FileSystem : URI {}", fileSystem.getUri().toString());

        Properties properties = new LinkedProperties();
        if(!path.isEmpty()){
            System.out.println("Reading Workflow Properties from file " + path);
            try(FSDataInputStream fsDataInputStream = fileSystem.open(new Path(path)) ){
                properties.load(fsDataInputStream);
            } catch (IOException e) {
                System.out.println("Failed to Read Workflow Properties from file " + path);
            }
        }

        Map<String, String> allProperties = new LinkedHashMap<>();
        properties.forEach((key, value) -> allProperties.put(key.toString(), value.toString()));
        log.info("Read {} properties ", allProperties.size());
        allProperties.forEach((k, v) -> System.out.println("***** Property " + k + " set to ****** " + v));
        return allProperties;
    }

}
