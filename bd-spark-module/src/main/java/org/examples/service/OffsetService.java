/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.examples.service;

import org.apache.kafka.common.TopicPartition;
import org.codejargon.fluentjdbc.api.mapper.Mappers;
import org.codejargon.fluentjdbc.api.query.Query;
import org.examples.utils.Heartbeats;

/**
 *
 * @author brijeshdhaker
 */
public class OffsetService {

    private static final String TABLE = "KAFKA_OFFSETS";

    private Query query;

    public OffsetService(Query query) {

        try {
            this.query = query;
            Heartbeats.checkTable(query, TABLE);
        } catch (Exception e){

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
        return 0L;
    }
}
