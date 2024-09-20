package org.examples.workflows;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.api.java.JavaStreamingContext$;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.spark.streaming.kafka010.LocationStrategy;
import org.examples.config.WorkflowConfig;
import org.examples.processor.StreamJobProcessor;

import java.util.Map;

public abstract class AbstractStreamWorkflow<Key, Value, RddEntity> {

    protected WorkflowConfig workflowConfig;

    public AbstractStreamWorkflow(WorkflowConfig workflowConfig){
        this.workflowConfig = workflowConfig;
    }

    public abstract void startWorkflow() throws Exception;
    
    protected abstract StreamJobProcessor<Key, Value, RddEntity> streamProcessor();

    protected abstract Map<String, Object> kafkaConfig();


    protected void checkToStop(JavaStreamingContext ssc){

    }
}
