package org.examples.utils;

import org.examples.enums.MessageFormat;
import org.examples.enums.MessageSource;
import org.examples.enums.MessageType;
import org.examples.enums.Zone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.regex.Pattern;

public class HadoopPathBuilder {


    private static final Logger logger = LoggerFactory.getLogger(HadoopPathBuilder.class);
    private static final Pattern pattern = Pattern.compile("hdfs:///ib/core/\\w+/data/(raw|refined|enriched)/(transacation|order)/");

    private String rootPath;
    private TimeProvider timeProvider;
    private String instance;
    private Zone zone;
    private MessageType messageType;
    private MessageSource messageSource;
    private MessageFormat messageFormat;

    private HadoopPathBuilder(String rootPath, TimeProvider timeProvider){
        this.rootPath = rootPath;
        this.timeProvider = timeProvider;
    }

    public static HadoopPathBuilder rootPath(String rootPath){
        return new HadoopPathBuilder(rootPath, new TimeProvider() {});
    }

    public static HadoopPathBuilder rootPath(String rootPath, TimeProvider timeProvider){
        return new HadoopPathBuilder(rootPath, timeProvider);
    }

    public HadoopPathBuilder instance(String instance){
        this.instance = instance;
        return this;
    }

    public HadoopPathBuilder zone(Zone zone){
        this.zone = zone;
        return this;
    }

    public HadoopPathBuilder messageType(MessageType messageType){
        this.messageType = messageType;
        return this;
    }

    public HadoopPathBuilder messageSource(MessageSource messageSource){
        this.messageSource = messageSource;
        return this;
    }

    public HadoopPathBuilder messageFormat(MessageFormat messageFormat){
        this.messageFormat = messageFormat;
        return this;
    }


    public String build(){
        return build(false);
    }

    public String build(boolean verifyPath){
        String path = new StringBuilder(rootPath)
                .append("/")
                .append(zone.name().toLowerCase(Locale.ENGLISH))
                .append("/")
                .append(messageType.name().toLowerCase(Locale.ENGLISH))
                .append("/")
                .append(messageFormat.name().toLowerCase(Locale.ENGLISH))
                .append("/")
                .append(messageSource.name().toLowerCase(Locale.ENGLISH))
                .append("/")
                .append("sandbox")
                .append("-")
                .append(timeProvider.nowAsString())
                .toString();

        return path;

    }

}
