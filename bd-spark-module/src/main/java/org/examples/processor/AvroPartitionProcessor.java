package org.examples.processor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class AvroPartitionProcessor implements FlatMapFunction<Iterator<ConsumerRecord<String, byte[]>>, Row> {

    @Override
    public Iterator<Row> call(Iterator<ConsumerRecord<String, byte[]>> consumerRecordIterator) throws Exception {
        long startTime = System.currentTimeMillis();

        List<Row> successful = new ArrayList<>();
        AtomicInteger processing = new AtomicInteger(0);
        consumerRecordIterator.forEachRemaining(record -> {
            processing.incrementAndGet();
            successful.add(toRawRow(record));
        });

        return successful.iterator();
    }


    private Row toRawRow(ConsumerRecord<String, byte[]> record){

        byte[] avroMessage = record.value();
        String errorTopic = "";

        return new GenericRowWithSchema(new Object[]{
                record.key(),
                avroMessage,
                record.topic(),
                errorTopic,
                1L,
                ""
        }, new StructType());
    }
}
