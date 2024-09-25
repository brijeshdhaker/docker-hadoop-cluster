package org.examples.schema;

import io.confluent.kafka.schemaregistry.client.rest.RestService;
import io.confluent.kafka.schemaregistry.client.rest.entities.Schema;
import org.apache.spark.sql.avro.SchemaConverters;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

public class SchemaProvider {


    private static final Logger logger = LoggerFactory.getLogger(SchemaProvider.class);
    private static final String SCHEMA_REGISTRY_URL = "http://schemaregistry.sandbox.net:8081";

    public static StructType structType(String topic_name) throws Exception {

            //Create REST service to access schema registry and retrieve topic schema (latest)
            RestService restService = new RestService(SCHEMA_REGISTRY_URL);
            Schema schema = restService.getLatestVersion(topic_name + "-value");
            String avroSchema = schema.getSchema();
            //StructType txn_schema = (StructType) StructType.fromJson(avroSchema);
            //System.out.println("txn_schema : " + txn_schema);

            StructType schemaType = (StructType) SchemaConverters.toSqlType(org.apache.avro.Schema.parse(avroSchema)).dataType();
            System.out.println("schemaType : " + schemaType);
            return schemaType;

    }

    public static StructType structTypeFromAvro(String path) throws Exception {

        String avro_schema = new String(Files.readAllBytes(Paths.get(path)));
        StructType schemaType = (StructType) SchemaConverters.toSqlType(org.apache.avro.Schema.parse(avro_schema)).dataType();
        System.out.println("schemaType : " + schemaType);
        return schemaType;

    }


    /**
     *
     StructType details = new StructType(
     new StructField[]{
     new StructField("subject", DataTypes.StringType, false, Metadata.empty()),
     new StructField("grade", DataTypes.StringType, false, Metadata.empty()),
     new StructField("remark", DataTypes.StringType, false, Metadata.empty())
     });

     StructType schema = new StructType()
     .add("id", DataTypes.StringType)
     .add("uuid", DataTypes.StringType)
     .add("cardtype", DataTypes.StringType)
     .add("website", DataTypes.StringType)
     .add("product", DataTypes.StringType)
     .add("amount", DataTypes.DoubleType)
     .add("city", DataTypes.StringType)
     .add("country", DataTypes.StringType)
     .add("addts", DataTypes.LongType);

     **/
}
