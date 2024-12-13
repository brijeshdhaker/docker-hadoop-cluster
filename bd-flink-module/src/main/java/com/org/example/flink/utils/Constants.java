package com.org.example.flink.utils;

import org.apache.flink.table.types.logical.BigIntType;
import org.apache.flink.table.types.logical.FloatType;
import org.apache.flink.table.types.logical.RowType;
import org.apache.flink.table.types.logical.VarCharType;

import java.util.Arrays;

public interface Constants {

    public static final String WORKFLOW_NAME = "workflow-name";
    public static final String ENGINE_TYPE = "engine-type";

    String LOCAL_CLUSTER = "local-cluster";
    String MINI_CLUSTER = "mini-cluster";
    String REMOTE_CLUSTER = "remote-cluster";

    RowType RAW_TXN_SCHEMA_ROW_TYPE = new RowType(Arrays.asList(
            new RowType.RowField("id", new BigIntType()),
            new RowType.RowField("uuid", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("cardType", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("website", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("product", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("amount", new FloatType()),
            new RowType.RowField("city", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("country", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("eventTime", new BigIntType())
    ));

    RowType REFINED_TXN_SCHEMA_ROW_TYPE = new RowType(Arrays.asList(
            new RowType.RowField("id", new BigIntType()),
            new RowType.RowField("uuid", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("cardType", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("website", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("product", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("amount", new FloatType()),
            new RowType.RowField("city", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("country", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("eventTime", new BigIntType()),
            new RowType.RowField("addts", new BigIntType())
    ));

    RowType ENRICH_TXN_SCHEMA_ROW_TYPE = new RowType(Arrays.asList(
            new RowType.RowField("id", new BigIntType()),
            new RowType.RowField("uuid", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("cardType", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("website", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("product", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("amount", new FloatType()),
            new RowType.RowField("city", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("country", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("eventTime", new BigIntType()),
            new RowType.RowField("addts", new BigIntType())
    ));
}
