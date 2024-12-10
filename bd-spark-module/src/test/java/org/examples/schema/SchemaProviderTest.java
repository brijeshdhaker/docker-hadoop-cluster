package org.examples.schema;

import org.apache.spark.sql.types.StructType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SchemaProviderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void structType() {

    }

    @Test
    public void structTypeFromAvro() throws Exception {

        StructType structType = SchemaProvider.structTypeFromAvro("/apps/sandbox/schema-registry/avro/transaction-record.avsc");
        assertNotNull(structType);

    }
}