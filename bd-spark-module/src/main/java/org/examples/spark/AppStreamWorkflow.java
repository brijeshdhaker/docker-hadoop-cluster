package org.examples.spark;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.examples.spark.streaming.structure.AvroDeserializer;
import scala.Tuple2;
import org.apache.avro.generic.GenericRecord;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.apache.commons.io.IOUtils.length;
import static org.apache.commons.lang.StringUtils.substring;

public class AppStreamWorkflow {


    public static void main(String[] arg){


        SparkSession spark = SparkSession
          .builder()
          .master("local[*]")  // // spark://spark-iceberg.sandbox.net:7077")
          .appName("AppStreamWorkflow")
//          .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")
          .config("spark.eventLog.enabled", "true")
          .config("spark.jars.ivy", "/apps/.ivy2")
          .config("spark.eventLog.dir", "/apps/var/logs/spark-events")
          .config("spark.history.fs.logDirectory", "/apps/var/logs/spark-events")
         .getOrCreate();

        // spark.sparkContext().setLogLevel("ERROR");

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", "kafkabroker.sandbox.net:9092");
        //kafkaParams.put("key.deserializer", org.apache.kafka.common.serialization.StringDeserializer.class);
        kafkaParams.put("key.deserializer", io.confluent.kafka.serializers.KafkaAvroDeserializer.class);
        kafkaParams.put("value.deserializer", io.confluent.kafka.serializers.KafkaAvroDeserializer.class);
        kafkaParams.put("group.id", "transaction-avro-cg");
        kafkaParams.put("auto.offset.reset", "earliest");
        kafkaParams.put("enable.auto.commit", false);
        kafkaParams.put("schema.registry.url", "http://schemaregistry.sandbox.net:8081");
        /*
        kafkaParams.put("heartbeat.interval.ms", 10_000);
        kafkaParams.put("session.timeout.ms", 5_000);
        kafkaParams.put("security.protocol", "SSL");
        kafkaParams.put("ssl.truststore.location", "/some-directory/kafka.client.truststore.jks");
        kafkaParams.put("ssl.truststore.password", "test1234");
        kafkaParams.put("ssl.keystore.location", "/some-directory/kafka.client.keystore.jks");
        kafkaParams.put("ssl.keystore.password", "test1234");
        kafkaParams.put("ssl.key.password", "test1234");
        */

        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(spark.sparkContext());
        JavaStreamingContext streamingContext = new JavaStreamingContext(jsc, Durations.seconds(10));

        Collection<String> topics = Arrays.asList("transaction-avro-topic");

        JavaInputDStream<ConsumerRecord<String, GenericRecord>> stream = KafkaUtils.createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, GenericRecord>Subscribe(topics, kafkaParams)
                );

        SchemaRegistryClient sclient = new CachedSchemaRegistryClient("http://schemaregistry:8081", 128);
        AvroDeserializer avroDeserializer = new AvroDeserializer(sclient);

        stream.foreachRDD(inputRDD -> {
            inputRDD.foreach(r -> {
                System.out.println("Key : " + r.key() + "  *******  Value : " + r.value());
            });
        });

        JavaPairDStream<String, GenericRecord> pairRDD  = stream.mapToPair(record -> {
            return new Tuple2<>(record.key(), record.value());
        });

        /*
        JavaPairDStream<String, byte[]> s1  = stream.mapToPair(record -> {
            return new Tuple2<>(record.key(), record.value());
        });

        s1.foreachRDD(r -> {
            List<Tuple2<String, byte[]>> touples = r.collect();
            touples.forEach(c -> {
                String key = avroDeserializer.deserialize(c._1().getBytes());
                String value = avroDeserializer.deserialize(c._2());
                System.out.println("Key : " + key + " *******  Value : " +  value );
            });
        });
        */

        streamingContext.start();              // Start the computation
        try {
            streamingContext.awaitTermination();   // Wait for the computation to terminate
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
