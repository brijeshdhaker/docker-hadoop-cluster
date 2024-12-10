package org.examples.context;

import org.apache.spark.sql.types.StructType;
import org.examples.enums.MessageSource;
import org.examples.enums.MessageType;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RawContext implements Serializable {

    private long jobId;
    private long jobStepId;
    private MessageType messageType;
    private MessageSource messageSource;
    private LocalDateTime transactionTime;
    private StructType schema;

    public static RawContext as(){
        return  new RawContext();
    }


    public long jobId(){
        return jobId;
    }

    public RawContext jobId(long jobId){
        this.jobId = jobId;
        return this;
    }

    public long jobStepId(){
        return jobStepId;
    }

    public RawContext jobStepId(long jobStepId){
        this.jobStepId = jobStepId;
        return this;
    }

    public MessageType messageType(){
        return messageType;
    }

    public RawContext messageType(MessageType messageType){
        this.messageType = messageType;
        return this;
    }


    public MessageSource messageSource(){
        return messageSource;
    }

    public RawContext messageSource(MessageSource messageSource){
        this.messageSource = messageSource;
        return this;
    }

    public LocalDateTime transactionTime(){
        return transactionTime;
    }

    public RawContext transactionTime(LocalDateTime transactionTime){
        this.transactionTime = transactionTime;
        return this;
    }

    public StructType schema(){
        return schema;
    }

    public RawContext schema(StructType schema){
        this.schema = schema;
        return this;
    }


}
