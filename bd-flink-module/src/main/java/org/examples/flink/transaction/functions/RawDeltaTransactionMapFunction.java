package org.examples.flink.transaction.functions;

import org.examples.flink.transaction.models.raw.RawTransaction;
import org.examples.flink.utils.Utils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.data.util.DataFormatConverters;
import org.apache.flink.table.types.logical.BigIntType;
import org.apache.flink.table.types.logical.FloatType;
import org.apache.flink.table.types.logical.RowType;
import org.apache.flink.table.types.logical.VarCharType;
import org.apache.flink.table.types.utils.TypeConversions;
import org.apache.flink.types.Row;

import java.util.Arrays;

/**
 * Internal class providing mock implementation for example stream source.
 * <p>
 * This streaming source will be generating events of type {@link Utils#FULL_SCHEMA_ROW_TYPE} with
 * interval of {@link RawDeltaTransactionMapFunction#NEXT_ROW_INTERVAL_MILLIS} that will be further
 * fed to the Flink job until the parent process is stopped.
 */
public class RawDeltaTransactionMapFunction implements MapFunction<RawTransaction, RowData> {

    static int NEXT_ROW_INTERVAL_MILLIS = 800;

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

    DataFormatConverters.DataFormatConverter<RowData, Row> CONVERTER =
            DataFormatConverters.getConverterForDataType(
                    TypeConversions.fromLogicalToDataType(RAW_TXN_SCHEMA_ROW_TYPE)
            );

    @Override
    public RowData map(RawTransaction raw_txn) throws Exception {
        RowData row = CONVERTER.toInternal(Row.of(
                raw_txn.getId(),
                raw_txn.getUuid(),
                raw_txn.getCardType(),
                raw_txn.getWebsite(),
                raw_txn.getProduct(),
                raw_txn.getAmount(),
                raw_txn.getCity(),
                raw_txn.getCountry(),
                raw_txn.getEventTime()
        ));
        return row;
    }
}
