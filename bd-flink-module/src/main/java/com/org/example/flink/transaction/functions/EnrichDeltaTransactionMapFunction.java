package com.org.example.flink.transaction.functions;

import com.org.example.flink.transaction.models.enriched.EnrichedTransaction;
import com.org.example.flink.transaction.models.raw.RawTransaction;
import com.org.example.flink.utils.Constants;
import com.org.example.flink.utils.Utils;
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
 * interval of {@link EnrichDeltaTransactionMapFunction#NEXT_ROW_INTERVAL_MILLIS} that will be further
 * fed to the Flink job until the parent process is stopped.
 */
public class EnrichDeltaTransactionMapFunction implements MapFunction<EnrichedTransaction, RowData> {

    static int NEXT_ROW_INTERVAL_MILLIS = 800;

    DataFormatConverters.DataFormatConverter<RowData, Row> CONVERTER =
            DataFormatConverters.getConverterForDataType(
                    TypeConversions.fromLogicalToDataType(Constants.ENRICH_TXN_SCHEMA_ROW_TYPE)
            );

    @Override
    public RowData map(EnrichedTransaction etxn) throws Exception {
        RowData row = CONVERTER.toInternal(Row.of(
                etxn.getId(),
                etxn.getUuid(),
                etxn.getCardType(),
                etxn.getWebsite(),
                etxn.getProduct(),
                etxn.getAmount(),
                etxn.getCity(),
                etxn.getCountry(),
                etxn.getEventTime(),
                etxn.getAddts()
        ));
        return row;
    }
}
