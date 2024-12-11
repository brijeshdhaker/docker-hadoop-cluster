package com.org.example.flink.transaction.functions;

import com.org.example.flink.transaction.models.raw.RawTransaction;
import com.org.example.flink.utils.Utils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.data.util.DataFormatConverters;
import org.apache.flink.table.types.logical.BigIntType;
import org.apache.flink.table.types.logical.FloatType;
import org.apache.flink.table.types.logical.RowType;
import org.apache.flink.table.types.logical.VarCharType;
import org.apache.flink.table.types.utils.TypeConversions;
import org.apache.flink.types.Row;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Internal class providing mock implementation for example stream source.
 * <p>
 * This streaming source will be generating events of type {@link Utils#FULL_SCHEMA_ROW_TYPE} with
 * interval of {@link DeltaTransactionMapFunction#NEXT_ROW_INTERVAL_MILLIS} that will be further
 * fed to the Flink job until the parent process is stopped.
 */
public class DeltaTransactionMapFunction implements MapFunction<RawTransaction, RowData> {

    static int NEXT_ROW_INTERVAL_MILLIS = 800;

    RowType FULL_SCHEMA_ROW_TYPE = new RowType(Arrays.asList(
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

    DataFormatConverters.DataFormatConverter<RowData, Row> CONVERTER =
            DataFormatConverters.getConverterForDataType(
                    TypeConversions.fromLogicalToDataType(FULL_SCHEMA_ROW_TYPE)
            );

    @Override
    public RowData map(RawTransaction rtxn) throws Exception {
        RowData row = CONVERTER.toInternal(Row.of(
                rtxn.getId(),
                rtxn.getUuid(),
                rtxn.getCardType(),
                rtxn.getWebsite(),
                rtxn.getProduct(),
                rtxn.getAmount(),
                rtxn.getCity(),
                rtxn.getCountry(),
                rtxn.getEventTime()
        ));
        return row;
    }
}
