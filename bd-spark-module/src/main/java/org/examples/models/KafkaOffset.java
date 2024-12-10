package org.examples.models;

import java.sql.Timestamp;
import static org.examples.utils.CopyUtil.copy;

public class KafkaOffset {

    private String consumerGroup;
    private String topic;
    private int partition;
    private long offsetValue;
    private Timestamp commitTime;


    public static KafkaOffset as(){
        return new KafkaOffset();
    }

    public String consumerGroup(){
        return consumerGroup;
    }

    public KafkaOffset consumerGroup(String consumerGroup){
        this.consumerGroup = consumerGroup;
        return this;
    }

    public String topic(){
        return topic;
    }

    public KafkaOffset topic(String topic){
        this.topic = topic;
        return this;
    }

    public int partition(){
        return partition;
    }

    public KafkaOffset partition(int partition){
        this.partition = partition;
        return this;
    }

    public long offsetValue(){
        return offsetValue;
    }

    public KafkaOffset offsetValue(long offsetValue){
        this.offsetValue = offsetValue;
        return this;
    }

    public Timestamp commitTime(){
        return copy(commitTime);
    }

    public KafkaOffset commitTime(Timestamp commitTime){
        this.commitTime = commitTime;
        return this;
    }
}
