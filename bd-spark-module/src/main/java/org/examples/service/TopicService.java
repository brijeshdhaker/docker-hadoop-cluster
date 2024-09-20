/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.examples.service;

import org.codejargon.fluentjdbc.api.mapper.Mappers;
import org.codejargon.fluentjdbc.api.query.Query;
import org.examples.utils.Heartbeats;

/**
 *
 * @author brijeshdhaker
 */
public class TopicService {

    private static final String TABLE = "KAFKA_TOPICS";

    private Query query;

    public TopicService(Query query) {

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

}
