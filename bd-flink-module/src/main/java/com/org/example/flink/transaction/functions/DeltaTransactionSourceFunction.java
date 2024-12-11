package com.org.example.flink.transaction.functions;

import com.github.javafaker.Faker;
import com.org.example.flink.transaction.models.raw.RawTransaction;
import com.org.example.flink.transaction.models.refined.RefineTransaction;
import com.org.example.flink.transaction.source.TransactionGenerator;
import com.org.example.flink.utils.DataGenerator;
import com.org.example.flink.utils.RandomItem;
import com.org.example.flink.utils.Utils;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.data.util.DataFormatConverters;
import org.apache.flink.table.types.logical.BigIntType;
import org.apache.flink.table.types.logical.FloatType;
import org.apache.flink.table.types.logical.RowType;
import org.apache.flink.table.types.logical.VarCharType;
import org.apache.flink.table.types.utils.TypeConversions;
import org.apache.flink.types.Row;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Internal class providing mock implementation for example stream source.
 * <p>
 * This streaming source will be generating events of type {@link Utils#FULL_SCHEMA_ROW_TYPE} with
 * interval of {@link DeltaTransactionSourceFunction#NEXT_ROW_INTERVAL_MILLIS} that will be further
 * fed to the Flink job until the parent process is stopped.
 */
public class DeltaTransactionSourceFunction extends RichParallelSourceFunction<RowData> {

    static int NEXT_ROW_INTERVAL_MILLIS = 800;

    final RowType FULL_SCHEMA_ROW_TYPE = new RowType(Arrays.asList(
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

    final DataFormatConverters.DataFormatConverter<RowData, Row> CONVERTER =
            DataFormatConverters.getConverterForDataType(
                    TypeConversions.fromLogicalToDataType(FULL_SCHEMA_ROW_TYPE)
            );

    public static final int SLEEP_MILLIS_PER_EVENT = 10;
    private static final int BATCH_SIZE = 5;
    private volatile boolean running = true;
    private Instant limitingTimestamp = Instant.MAX;

    /** Create a bounded TaxiFareGenerator that runs only for the specified duration. */
    public static DeltaTransactionSourceFunction runFor(Duration duration) {
        DeltaTransactionSourceFunction generator = new DeltaTransactionSourceFunction();
        generator.limitingTimestamp = DataGenerator.BEGINNING.plus(duration);
        return generator;
    }



    @Override
    public void run(SourceContext<RowData> ctx) throws InterruptedException {

        PriorityQueue<RefineTransaction> endEventQ = new PriorityQueue<>(100);
        long id = 0;
        long maxStartTime = 0;

        Faker faker = new Faker(Locale.US);

        // Generate a batch of transactions
        List<RowData> txns = new ArrayList<RowData>(BATCH_SIZE);

        List<String> CCTYPES = Arrays.asList("VISA", "Master", "Amex", "RuPay", "Discover");
        List<String> PRODUCTS = Arrays.asList("Mobile", "Tablet", "Computer", "Laptop", "RAM", "TV", "Speaker", "Mouse", "Keyboard", "LDC", "Monitor", "Printer");
        List<String> COUNTRIES = Arrays.asList("IN", "USA", "UK", "JP");
        List<String> SITES = Arrays.asList("Amazon", "Flipkart", "SnapDeal", "Myntra", "Ebay");

        Map<String, List<String>> CITIES = new HashMap<>();
        CITIES.put("IN",Arrays.asList("Delhi", "Chennai", "Pune", "Mumbai", "Bengaluru"));
        CITIES.put("USA",Arrays.asList("New York", "Los Angeles", "Miami"));
        CITIES.put("UK",Arrays.asList("London", "Manchester", "Liverpool", "Oxford"));
        CITIES.put("JP",Arrays.asList("Tokyo", "Osaka", "Yokohama", "Hiroshima"));

        Map<String, String> URLS = new HashMap<>();
        URLS.put("Amazon", "https://www.amazon.in");
        URLS.put("Flipkart", "https://www.flipkart.com/");
        URLS.put("SnapDeal", "https://www.snapdeal.com/");
        URLS.put("Myntra","https://www.myntra.com/");
        URLS.put("Ebay", "https://www.ebay.com/");


        while (running) {

            for (int i = 1; i <= BATCH_SIZE; i++) {

                String ccType = RandomItem.getRandomItem(CCTYPES);
                String txnSite = RandomItem.getRandomItem(SITES);
                String txnProduct = RandomItem.getRandomItem(PRODUCTS);
                String txnCountry = RandomItem.getRandomItem(COUNTRIES);
                String txnCity = RandomItem.getRandomItem(CITIES.get(txnCountry));
                Long tid = id + i;

                RowData row = CONVERTER.toInternal(Row.of(
                        tid,
                        UUID.randomUUID().toString(),
                        ccType,
                        txnSite,
                        txnProduct,
                        new Random().nextFloat(01.00f, 200.00f),
                        txnCity,
                        txnCountry,
                        Instant.now().getEpochSecond()
                ));
                txns.add(row);
            }

            txns.iterator().forEachRemaining(r -> ctx.collect(r));

            // prepare for the next batch
            id += BATCH_SIZE;

            // don't go too fast
            Thread.sleep(BATCH_SIZE * SLEEP_MILLIS_PER_EVENT);

        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
