package org.examples.utils;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class AvroUtil {


    private static final Logger logger = LoggerFactory.getLogger(AvroUtil.class);

    public static Schema schemaFromString(String avro) {
        return new Schema.Parser().parse(avro);
    }

    public static Schema schemaFromFile(File avrofile) {
        try {
            return new Schema.Parser().parse(avrofile);
        } catch (IOException e) {
            logger.error("Error occurred while loading schema from avro file {}", avrofile, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert JSON to avro binary array.
     *
     * @param json
     * @param schema
     * @return
     * @throws IOException
     */
    public static byte[] jsonToAvro(String json, Schema schema) throws IOException {
        DatumReader<Object> reader = new GenericDatumReader<>(schema);
        GenericDatumWriter<Object> writer = new GenericDatumWriter<>(schema);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Decoder decoder = DecoderFactory.get().jsonDecoder(schema, json);
        Encoder encoder = EncoderFactory.get().binaryEncoder(output, null);
        Object datum = reader.read(null, decoder);
        writer.write(datum, encoder);
        encoder.flush();
        return output.toByteArray();
    }

    /**
     * Convert Avro binary byte array back to JSON String.
     *
     * @param avro
     * @param schema
     * @return
     * @throws IOException
     */
    public static String avroToJson(byte[] avro, Schema schema) throws IOException {
        boolean pretty = false;
        GenericDatumReader<Object> reader = new GenericDatumReader<>(schema);
        DatumWriter<Object> writer = new GenericDatumWriter<>(schema);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonEncoder encoder = EncoderFactory.get().jsonEncoder(schema, output, pretty);
        Decoder decoder = DecoderFactory.get().binaryDecoder(avro, null);
        Object datum = reader.read(null, decoder);
        writer.write(datum, encoder);
        encoder.flush();
        output.flush();
        return new String(output.toByteArray(), "UTF-8");
    }

}
