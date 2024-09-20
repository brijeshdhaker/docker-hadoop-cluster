package org.examples.utils;

import org.apache.kafka.common.TopicPartition;
import org.apache.spark.streaming.kafka010.OffsetRange;
import org.examples.models.KafkaOffset;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class KafkaUtil {

    public static List<TopicPartition> partitions(Map<String, Integer> numberOfPartitions){
        return numberOfPartitions.entrySet().stream()
                .flatMap(e -> partitions(e.getKey(), e.getValue()).stream())
                .collect(Collectors.toList());
    }

    private static List<TopicPartition> partitions(String topic, Integer numberOfPartitions) {
        return IntStream.range(0,numberOfPartitions)
                .mapToObj(n -> new TopicPartition(topic, n))
                .collect(Collectors.toList());
    }


    private static Map<TopicPartition, Long> unwrap(List<KafkaOffset> offsets) {
        return offsets.stream()
                .collect(Collectors.toMap(o -> new TopicPartition(o.topic(), o.partition()),
                KafkaOffset::offsetValue));
    }

    private static List<KafkaOffset> wrap(String cg, OffsetRange[] offsetRanges) {
        return Stream.of(offsetRanges)
                .filter(offsetRange -> offsetRange.untilOffset() != offsetRange.fromOffset())
                .map(offsetRange ->
                        KafkaOffset.as()
                                .consumerGroup(cg)
                                .topic(offsetRange.topic())
                                .partition(offsetRange.partition())
                                .offsetValue(offsetRange.untilOffset())
                                .commitTime(Timestamp.valueOf(LocalDateTime.now(Clock.systemUTC())))
                ).collect(Collectors.toList());
    }

    private static long count(OffsetRange[] offsetRanges) {
        return Stream.of(offsetRanges).map(offsetRange -> offsetRange.untilOffset() - offsetRange.fromOffset())
                .reduce((x,y) -> x + y).orElse(0L);
    }


}
