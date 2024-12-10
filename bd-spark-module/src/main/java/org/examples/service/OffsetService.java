/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.examples.service;

import org.apache.kafka.common.TopicPartition;
import org.codejargon.fluentjdbc.api.mapper.Mappers;
import org.codejargon.fluentjdbc.api.query.Query;
import org.examples.datasource.DataMappers;
import org.examples.models.KafkaOffset;
import org.examples.utils.Heartbeats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author brijeshdhaker
 */
public class OffsetService {


    private static final String TABLE = "KAFKA_OFFSETS";
    private static final Logger logger = LoggerFactory.getLogger(OffsetService.class);

    private Query query;

    public OffsetService(Query query) {

        try {
            this.query = query;
            Heartbeats.checkTable(query, TABLE);
        } catch (Exception e){
            logger.error(e.getMessage());
        }

    }

    public Query query(){
        return query;
    }

    public long count(){
        return query.select("SELECT COUNT(*) FROM  " + TABLE ).singleResult(Mappers.singleLong());
    }

    public long offset(String cg, TopicPartition topicPartition){
        return offset(cg, topicPartition.topic(), topicPartition.partition());
    }

    public long offset(String cg, String topic, int partition){
        boolean exist = exist(cg, topic, partition);
        if(!exist){
            logger.warn("Unable to find [] {} {} {}, initializing with 0 ", cg, topic, partition);
        }
        return exist ? offsetValue(cg, topic, partition) : initWith(cg, topic, partition, 0);
    }

    public long update(String cg, String topic , int partition, long offset, Timestamp commit_time){

        return query.update("UPDATE " + TABLE + " SET (OFFSET_VALUE = :offset,  COMMIT_TIME = :commitTime ) " +
                        " where (CONSUMER_GROUP = :consumer_group and TOPIC = :topic and  `PARTITION` = :partition) ")
                .namedParam("consumer_group",cg)
                .namedParam("topic", topic)
                .namedParam("partition", partition)
                .namedParam("offset_values", offset)
                .namedParam("commit_time", commit_time)
                .run().affectedRows();

    }

    public long initWith(String cg, String topic , int partition, long offset){

         query.update("INSERT INTO " + TABLE + " (CONSUMER_GROUP, TOPIC, PARTITION, OFFSET_VALUE) " +
                        " values (CONSUMER_GROUP = :consumer_group and TOPIC = :topic and  `PARTITION` = :partition) ")
                .namedParam("consumer_group",cg)
                .namedParam("topic", topic)
                .namedParam("partition", partition)
                .namedParam("offset_values", offset)
                .run();

        return 0L;
    }

    public boolean exist(String cg, String topic , int partition){
        return query.select("SELECT count(*) FROM " + TABLE
                        + " where CONSUMER_GROUP = :consumer_group and TOPIC = :topic and  `PARTITION` = :partition" )
                .namedParam("consumer_group",cg)
                .namedParam("topic", topic)
                .namedParam("partition", partition)
                .singleResult(Mappers.singleLong()) > 0;
    }

    public Timestamp commitTime(String cg, String topic , int partition){
        return query.select("SELECT COMMIT_TIME FROM " + TABLE
                        + " where CONSUMER_GROUP = :consumer_group and TOPIC = :topic and  `PARTITION` = :partition" )
                .namedParam("consumer_group",cg)
                .namedParam("topic", topic)
                .namedParam("partition", partition)
                .singleResult(DataMappers.timestamMapper);
    }

    public long offsetValue(String cg, String topic , int partition){
        return query.select("SELECT OFFSET_VALUE FROM " + TABLE +
                        " where CONSUMER_GROUP = :consumer_group and TOPIC = :topic and  `PARTITION` = :partition" )
                .namedParam("consumer_group",cg)
                .namedParam("topic", topic)
                .namedParam("partition", partition)
                .singleResult(Mappers.singleLong());
    }

    public List<KafkaOffset> all(String cg){
        return query.select("SELECT * FROM " + TABLE + " where CONSUMER_GROUP = :consumer_group" )
                .namedParam("consumer_group",cg)
                .listResult(DataMappers.kafkaOffsetMapper());
    }

    public List<KafkaOffset> all(String cg, List<String> topics){
        return query.select("SELECT * FROM " + TABLE + " where CONSUMER_GROUP = :consumer_group and TOPIC in (:topics)" )
                .namedParam("consumer_group",cg)
                .namedParam("topics", topics)
                .listResult(DataMappers.kafkaOffsetMapper());
    }

    public void update(List<KafkaOffset> offsets){
        if(!offsets.isEmpty() && offsets.size() > 0) {
            List<Map<String, ?>> params = offsets.stream()
                    .map(o -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("consumer_group", o.consumerGroup());
                        map.put("topic", o.topic());
                        map.put("partition", o.partition());
                        map.put("offset", o.offsetValue());
                        map.put("commit_time", o.commitTime());
                        return map;
                    }).collect(Collectors.toList());

            query.batch(" UPDATE " + TABLE + " set OFFSET_VALUE = :offset, COMMIT_TIME = :commit_time " +
                            "where CONSUMER_GROUP = :consumer_group and TOPIC = :topic and `PARTITION` = :partition").batchSize(offsets.size())
                    .namedParams(params)
                    .run();
        }
    }
}
