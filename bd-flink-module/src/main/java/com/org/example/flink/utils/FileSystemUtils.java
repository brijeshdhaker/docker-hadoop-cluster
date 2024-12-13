package com.org.example.flink.utils;


import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.runtime.util.HadoopUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.nio.file.FileSystemException;

public class FileSystemUtils {

    static FileSystem fs;

    static {
        try {
            fs = FileSystem.get(HadoopUtils.getHadoopConfiguration(GlobalConfiguration.loadConfiguration()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isExists(String path) throws FileSystemException {
        try {
            return fs.exists(new Path(path));
        } catch (IOException e) {
            throw new FileSystemException("Unable to check file existence ");
        }
    }

    public static void prepareDirs(String tablePath) throws IOException {
        Path tableDir = new Path(tablePath);
        if (fs.exists(tableDir)) {
            fs.delete(tableDir, true);
        } else {
            fs.mkdirs(tableDir);
        }
    }

    public static void prepareDirs(String sourcePath, String workPath) throws IOException {
        prepareDirs(workPath);
        System.out.printf("Copy example table data from %s to %s%n%n", sourcePath, workPath);
        Configuration conf = HadoopUtils.getHadoopConfiguration(GlobalConfiguration.loadConfiguration());
        boolean copy = FileUtil.copy(fs, new Path(sourcePath), fs, new Path(workPath), false, conf);
    }


    public static void copyDir(String source, String dest){

        Path sourcePath = new Path(source);
        Path destPath = new Path(dest);
        try {
            boolean isExists = fs.exists(sourcePath);
            FileStatus[] status =  fs.listStatus(sourcePath);
            for (FileStatus fls : status) {
                FileUtil.copy(fs, fls.getPath(), fs, destPath, true, fs.getConf());
                System.out.printf("Copy file from %s to %s%n%n", source, dest);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
