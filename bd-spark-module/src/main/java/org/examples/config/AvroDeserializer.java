package org.examples.config;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AvroDeserializer <T extends SpecificRecordBase> implements Deserializer {


    private static final Logger logger = LoggerFactory.getLogger(AvroDeserializer.class);
    protected final Class<T> targetType;

    public AvroDeserializer(Class<T> targetType) {
        this.targetType = targetType;
    }

    @Override
    public void configure(Map configs, boolean isKey) {
        // do nothing
    }

    @Override
    public T deserialize(String topic, byte[] bytes) {
        T returnObject = null;

        try {

            if (bytes != null) {
                DatumReader<GenericRecord> datumReader = new SpecificDatumReader<>(targetType.newInstance().getSchema());
                Decoder decoder = DecoderFactory.get().binaryDecoder(bytes, null);
                returnObject = (T) datumReader.read(null, decoder);
                logger.info("deserialized data='{}'", returnObject.toString());
            }
        } catch (Exception e) {
            logger.error("Unable to Deserialize bytes[] ", e);
        }

        return returnObject;
    }

    @Override
    public void close() {
        // do nothing
    }
}