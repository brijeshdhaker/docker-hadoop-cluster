package org.examples.flink.transaction.functions;

import org.examples.flink.transaction.models.enriched.EnrichedTransaction;
import org.examples.flink.transaction.models.refined.RefineTransaction;
import org.examples.flink.utils.Constants;
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
 * interval of {@link RefineDeltaTransactionMapFunction#NEXT_ROW_INTERVAL_MILLIS} that will be further
 * fed to the Flink job until the parent process is stopped.
 */
public class RefineDeltaTransactionMapFunction implements MapFunction<RefineTransaction, RowData> {

    static int NEXT_ROW_INTERVAL_MILLIS = 800;

    DataFormatConverters.DataFormatConverter<RowData, Row> CONVERTER =
            DataFormatConverters.getConverterForDataType(
                    TypeConversions.fromLogicalToDataType(Constants.REFINED_TXN_SCHEMA_ROW_TYPE)
            );

    @Override
    public RowData map(RefineTransaction re_txn) throws Exception {
        RowData row = CONVERTER.toInternal(Row.of(
                re_txn.getId(),
                re_txn.getUuid(),
                re_txn.getCardType(),
                re_txn.getWebsite(),
                re_txn.getProduct(),
                re_txn.getAmount(),
                re_txn.getCity(),
                re_txn.getCountry(),
                re_txn.getEventTime(),
                re_txn.getAddts()
        ));
        return row;
    }
}
