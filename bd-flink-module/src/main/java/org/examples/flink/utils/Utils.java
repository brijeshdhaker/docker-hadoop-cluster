package org.examples.flink.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.runtime.util.HadoopUtils;
import org.apache.flink.table.types.logical.IntType;
import org.apache.flink.table.types.logical.RowType;
import org.apache.flink.table.types.logical.VarCharType;
import org.apache.flink.types.Row;

import io.delta.standalone.DeltaLog;
import io.delta.standalone.Snapshot;
import io.delta.standalone.data.CloseableIterator;
import io.delta.standalone.data.RowRecord;
import org.apache.hadoop.conf.Configuration;

public final class Utils {

    static int PRINT_PAD_LENGTH = 4;

    private Utils() {}

    public static final RowType FULL_SCHEMA_ROW_TYPE = new RowType(Arrays.asList(
            new RowType.RowField("f1", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("f2", new VarCharType(VarCharType.MAX_LENGTH)),
            new RowType.RowField("f3", new IntType())
    ));

    public static String resolveTableAbsolutePath(String resource_path, String engine_type) {
        //String rootPath = Paths.get(".").toAbsolutePath().normalize().toString();
        String rootPath = engine_type.equalsIgnoreCase("local-cluster") ? "file:///apps/sandbox/defaultfs" : "s3a://defaultfs";
        return rootPath + "/" + resource_path;
    }

    public static void prepareDirs(String tablePath) throws IOException {
        File tableDir = new File(tablePath);
        if (tableDir.exists()) {
            FileUtils.cleanDirectory(tableDir);
        } else {
            tableDir.mkdirs();
        }
    }

    public static void prepareDirs(String sourcePath, String workPath) throws IOException {
        prepareDirs(workPath);
        System.out.printf("Copy example table data from %s to %s%n%n", sourcePath, workPath);
        FileUtils.copyDirectory(new File(sourcePath), new File(workPath));
    }

    public static ScheduledFuture<?> runSourceTableUpdater(String tablePath) {

        final DeltaTableUpdater tableUpdater = new DeltaTableUpdater(tablePath);

        AtomicInteger index = new AtomicInteger(0);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        return scheduler.scheduleWithFixedDelay(
                () -> {
                    int i = index.getAndIncrement();
                    List<Row> rows = Collections.singletonList(
                            Row.of("f1_newVal_" + i, "f2_newVal_" + i, i));
                    Descriptor descriptor = new Descriptor(tablePath, Utils.FULL_SCHEMA_ROW_TYPE, rows);
                    tableUpdater.writeToTable(descriptor);
                },
                10,
                2,
                TimeUnit.SECONDS
        );
    }

    public static Configuration getHadoopFsConfiguration(String engine_type){

        Configuration configuration = new Configuration();
        if(engine_type.equalsIgnoreCase(Constants.REMOTE_CLUSTER) || engine_type.equalsIgnoreCase(Constants.MINI_CLUSTER)){
            configuration.set("fs.defaultFS", "s3a://defaultfs");
            configuration.set("fs.s3a.endpoint", "http://minio.sandbox.net:9010");
            configuration.set("fs.s3a.access.key", "pgm2H2bR7a5kMc5XCYdO");
            configuration.set("fs.s3a.secret.key", "zjd8T0hXFGtfemVQ6AH3yBAPASJNXNbVSx5iddqG");
            configuration.set("fs.s3a.path.style.access", "true");
            configuration.set("fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");
            configuration.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
        }else {
            configuration.set("fs.defaultFS", "file:///apps/sandbox/defaultfs");
        }
        return configuration;
    }

    public static void printDeltaTableRows(String tablePath, String engine_type) throws InterruptedException {

        Configuration configuration = getHadoopFsConfiguration(engine_type);
        DeltaLog deltaLog = DeltaLog.forTable(configuration, tablePath);

        for (int i = 0; i < 30; i++) {
            deltaLog.update();
            Snapshot snapshot = deltaLog.snapshot();

            System.out.println("===== current snapshot =====");
            System.out.println("snapshot version: " + snapshot.getVersion());
            System.out.println("number of total data files: " + snapshot.getAllFiles().size());

            CloseableIterator<RowRecord> iter = snapshot.open();
            System.out.println("\ntable rows:");
            System.out.println(StringUtils.rightPad("f1", PRINT_PAD_LENGTH) + "| " +
                    StringUtils.rightPad("f2", PRINT_PAD_LENGTH) + " | " +
                    StringUtils.rightPad("f3", PRINT_PAD_LENGTH));
            System.out.println(String.join("", Collections.nCopies(4 * PRINT_PAD_LENGTH, "-")));

            RowRecord row = null;
            int numRows = 0;
            while (iter.hasNext()) {
                row = iter.next();
                numRows++;

                String f1 = row.isNullAt("f1") ? null : row.getString("f1");
                String f2 = row.isNullAt("f2") ? null : row.getString("f2");
                Integer f3 = row.isNullAt("f3") ? null : row.getInt("f3");

                System.out.println(StringUtils.rightPad(f1, PRINT_PAD_LENGTH) + "| " +
                        StringUtils.rightPad(f2, PRINT_PAD_LENGTH) + " | " +
                        StringUtils.rightPad(String.valueOf(f3), PRINT_PAD_LENGTH));
            }
            System.out.println("\nnumber rows: " + numRows);
            if (row != null) {
                System.out.println("data schema:");
                System.out.println(row.getSchema().getTreeString());
                System.out.println("partition cols:");
                System.out.println(snapshot.getMetadata().getPartitionColumns());
            }
            System.out.println("\n");
            Thread.sleep(5000);
        }
    }
}
