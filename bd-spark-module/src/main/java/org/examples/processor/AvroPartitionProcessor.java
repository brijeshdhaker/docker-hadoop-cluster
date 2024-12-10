package org.examples.processor;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.examples.context.RawContext;
import org.examples.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import static org.apache.spark.sql.avro.functions.*;

public final class AvroPartitionProcessor implements FlatMapFunction<Iterator<ConsumerRecord<String, GenericRecord>>, Row> {


    private static final Logger logger = LoggerFactory.getLogger(AvroPartitionProcessor.class);

    private static final long serialVersionUID = -1341929071565989551L;

    private RawContext rawContext;
    private Map<String, String> errorTopics;

    public AvroPartitionProcessor(RawContext rawContext, Map<String, String> errorTopics) {
        this.rawContext = rawContext;
        this.errorTopics = errorTopics;
    }


    public static AvroPartitionProcessor as(RawContext rawContext, Map<String, String> errorTopics) {
        return new AvroPartitionProcessor(rawContext, errorTopics);
    }

    @Override
    public Iterator<Row> call(Iterator<ConsumerRecord<String, GenericRecord>> consumerRecordIterator) throws Exception {
        long startTime = System.currentTimeMillis();
        logger.debug("jobId: {} | Starting partition call", rawContext.jobId());

        List<Row> successful = new ArrayList<>();
        AtomicInteger processing = new AtomicInteger(0);

        consumerRecordIterator.forEachRemaining(record -> {
            processing.incrementAndGet();

            successful.add(toRawRow(record));
            logger.debug("jobId: {} successfully processed record for key [{}] ", rawContext.jobId(), record.key());
        });

        if(processing.get() > 0 ){
            logger.info("jobId: {} Partition processed: {}, successful: {}, failed: {}, within time: {}", rawContext.jobId(), processing.get(), successful.size(),
            processing.get()-successful.size(), TimeUtil.diffInMinAndSec(startTime, System.currentTimeMillis()));
        }

        return successful.iterator();
    }


    private Row toRawRow(ConsumerRecord<String, GenericRecord> record){

        //byte[] avroMessage = record.value();
        String errorTopic = errorTopics.get(record.topic());

        return new GenericRowWithSchema(new Object[]{
                record.key(),
                record.value(),
                record.topic(),
                errorTopic,
                rawContext.jobStepId(),
                rawContext.messageSource().name()

        }, rawContext.schema());
    }
}
