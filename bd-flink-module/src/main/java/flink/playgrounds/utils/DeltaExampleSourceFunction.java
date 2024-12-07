package flink.playgrounds.utils;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.data.util.DataFormatConverters;
import org.apache.flink.table.types.utils.TypeConversions;
import org.apache.flink.types.Row;

/**
 * Internal class providing mock implementation for example stream source.
 * <p>
 * This streaming source will be generating events of type {@link Utils#FULL_SCHEMA_ROW_TYPE} with
 * interval of {@link DeltaExampleSourceFunction#NEXT_ROW_INTERVAL_MILLIS} that will be further
 * fed to the Flink job until the parent process is stopped.
 */
public class DeltaExampleSourceFunction extends RichParallelSourceFunction<RowData> {

    static int NEXT_ROW_INTERVAL_MILLIS = 800;

    public static final DataFormatConverters.DataFormatConverter<RowData, Row> CONVERTER =
            DataFormatConverters.getConverterForDataType(
                    TypeConversions.fromLogicalToDataType(Utils.FULL_SCHEMA_ROW_TYPE)
            );

    private volatile boolean cancelled = false;

    @Override
    public void run(SourceContext<RowData> ctx) throws InterruptedException {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        while (!cancelled) {

            RowData row = CONVERTER.toInternal(
                    Row.of(
                            String.valueOf(random.nextInt(0, 10)),
                            String.valueOf(random.nextInt(0, 100)),
                            random.nextInt(0, 30))
            );
            ctx.collect(row);
            Thread.sleep(NEXT_ROW_INTERVAL_MILLIS);
        }
    }

    @Override
    public void cancel() {
        cancelled = true;
    }
}
