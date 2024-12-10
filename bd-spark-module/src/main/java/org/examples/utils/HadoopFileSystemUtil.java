package org.examples.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystemException;

public class HadoopFileSystemUtil {


    private static final Logger logger = LoggerFactory.getLogger(HadoopFileSystemUtil.class);
    private final static boolean local = Boolean.parseBoolean(System.getProperty("workflow.app.local"));


    public static boolean isExists(String path, Configuration hadoopConfiguration) throws FileSystemException {
        try {
            return FileSystem.get(hadoopConfiguration).exists(new Path(path));
        } catch (IOException e) {
            throw new FileSystemException("Unable to check file existence ");
        }
    }

}
