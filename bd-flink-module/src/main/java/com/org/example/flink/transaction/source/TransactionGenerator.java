package com.org.example.flink.transaction.source;

import com.github.javafaker.Faker;
import com.org.example.flink.transaction.models.refined.RefineTransaction;
import com.org.example.flink.transaction.models.raw.RawTransaction;
import com.org.example.flink.utils.DataGenerator;
import com.org.example.flink.utils.RandomItem;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class TransactionGenerator implements SourceFunction<RawTransaction> {

    public static final int SLEEP_MILLIS_PER_EVENT = 10;
    private static final int BATCH_SIZE = 5;
    private volatile boolean running = true;
    private Instant limitingTimestamp = Instant.MAX;

    /** Create a bounded TaxiFareGenerator that runs only for the specified duration. */
    public static TransactionGenerator runFor(Duration duration) {
        TransactionGenerator generator = new TransactionGenerator();
        generator.limitingTimestamp = DataGenerator.BEGINNING.plus(duration);
        return generator;
    }

    @Override
    public void run(SourceContext<RawTransaction> sourceContext) throws Exception {

        PriorityQueue<RefineTransaction> endEventQ = new PriorityQueue<>(100);
        long id = 0;
        long maxStartTime = 0;

        Faker faker = new Faker(Locale.US);

        // Generate a batch of transactions
        List<RawTransaction> txns = new ArrayList<RawTransaction>(BATCH_SIZE);

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

                RawTransaction transaction = RawTransaction.builder().id(id + i)
                        .amount(new Random().nextFloat(01.00f, 200.00f))
                        .cardtype(ccType)
                        .website(txnSite)
                        .product(txnProduct)
                        .city(txnCity)
                        .country(txnCountry);

                txns.add(transaction);

                // the start times may be in order, but let's not assume that
                //maxStartTime = Math.max(maxStartTime, ride.getEventTimeMillis());
            }

            txns.iterator().forEachRemaining(r -> sourceContext.collect(r));

            // prepare for the next batch
            id += BATCH_SIZE;

            // don't go too fast
            Thread.sleep(BATCH_SIZE * SLEEP_MILLIS_PER_EVENT);

        }
    }

    @Override
    public void cancel() {

    }
}
