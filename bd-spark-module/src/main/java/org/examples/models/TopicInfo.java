package org.examples.models;

public class TopicInfo {

    private String errorTopic;
    private String mainTopic;
    private Integer partitions;

    public static TopicInfo as(){
        return new TopicInfo();
    }

    public String mainTopic(){
        return mainTopic;
    }

    public TopicInfo mainTopic(String mainTopic){
        this.mainTopic = mainTopic;
        return this;
    }

    public String errorTopic(){
        return errorTopic;
    }

    public TopicInfo errorTopic(String errorTopic){
        this.errorTopic = errorTopic;
        return this;
    }

    public Integer partitions(){
        return partitions;
    }

    public TopicInfo partitions(Integer partitions){
        this.partitions = partitions;
        return this;
    }

}
