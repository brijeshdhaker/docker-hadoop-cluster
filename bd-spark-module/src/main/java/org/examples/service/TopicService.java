package org.examples.service;

import org.codejargon.fluentjdbc.api.mapper.Mappers;
import org.codejargon.fluentjdbc.api.query.Query;
import org.examples.datasource.DataMappers;
import org.examples.models.TopicInfo;
import org.examples.utils.Heartbeats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 *
 * @author brijeshdhaker
 */
public class TopicService {

    private static final Logger logger = LoggerFactory.getLogger(TopicService.class);

    private static final String TABLE = "KAFKA_TOPICS";

    private Map<String,Integer> partitions = Collections.emptyMap();
    private Map<String,String> error_topics = Collections.emptyMap();

    private Query query;

    public TopicService(Query query, List<String> topics) {
        try {
            this.query = query;
            Heartbeats.checkTable(query, TABLE);
        } catch (Exception e){

        }
    }

    public Map<String, Integer> partitions(){
        return partitions;
    }

    public Map<String,String> error_topics(){
        return error_topics;
    }

    public Query query(){
        return query;
    }

    public long count(){
        return query.select("SELECT COUNT(*) FROM  " + TABLE ).singleResult(Mappers.singleLong());
    }


    private void init(List<String> topics){

        List<TopicInfo> topicInfo = query.select("select * from " + TABLE + " where MAIN_TOPIC IN (:topics)" )
                .namedParam("topics", topics)
                .listResult(DataMappers.topicInfoMapper());

        if(topicInfo.size() != topics.size()){

        }

        partitions = topicInfo.stream()
                .filter(t -> isNotBlank(t.mainTopic()))
                .collect(Collectors.toMap(TopicInfo::mainTopic, TopicInfo::partitions));


        error_topics = topicInfo.stream()
                .filter(t -> isNotBlank(t.errorTopic()))
                .collect(Collectors.toMap(TopicInfo::mainTopic, TopicInfo::errorTopic));
    }

}
