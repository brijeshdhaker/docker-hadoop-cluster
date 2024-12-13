package org.examples.datasource;

import org.codejargon.fluentjdbc.api.mapper.ObjectMappers;
import org.codejargon.fluentjdbc.api.query.Mapper;
import org.examples.models.KafkaOffset;
import org.examples.models.TopicInfo;

import java.sql.Timestamp;

public class DataMappers {

    public DataMappers() {
    }

    private static final ObjectMappers objectMappers = ObjectMappers.builder().build();

    public static final Mapper<Timestamp> timestamMapper = rs -> rs.getTimestamp(1);
    public static final Mapper<String> stringMapper = rs -> rs.getString(1);
    public static final Mapper<Integer> integerMapper = rs -> rs.getInt(1);

    public static Mapper<KafkaOffset> kafkaOffsetMapper() {
        return objectMappers.forClass(KafkaOffset.class);
    }

    public static Mapper<TopicInfo> topicInfoMapper() {
        return objectMappers.forClass(TopicInfo.class);
    }

}
