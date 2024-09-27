package org.examples.utils;

import org.apache.avro.Schema;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class AvroUtilTest {

    private final String jsonStr = "{" + "\"name\":\"Frank\"," + "\"age\":47" + "}";
    private final String schemaStr = "{ \"type\":\"record\", \"namespace\":\"foo\", \"name\":\"Person\", \"fields\":[ { \"name\":\"name\", \"type\":\"string\" }, { \"name\":\"age\", \"type\":\"int\" } ] }";

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void schemaFromString() {
        Schema schema = AvroUtil.schemaFromString(schemaStr);
        assertNotNull(schema);
    }

    @Test
    public void schemaFromFile() {
        File avrofile = new File("/apps/sandbox/schema-registry/avro/transaction-record.avsc");
        Schema schema = AvroUtil.schemaFromFile(avrofile);
        assertNotNull(schema);
    }

    @Test
    public void jsonToAvro() throws IOException {
        Schema schema = AvroUtil.schemaFromString(schemaStr);
        byte[] data = AvroUtil.jsonToAvro(jsonStr, schema);

    }

    @Test
    public void avroToJson() throws IOException {
        Schema schema = AvroUtil.schemaFromString(schemaStr);
        byte[] data = AvroUtil.jsonToAvro(jsonStr, schema);
        String jsonString = AvroUtil.avroToJson(data, schema);
        assertEquals(jsonString, jsonStr);
        System.out.println(jsonString);
    }


}