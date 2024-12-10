package org.examples.workflows;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.examples.config.WorkflowConfig;

public class BatchWorkflow extends Workflow {

    public BatchWorkflow(WorkflowConfig workflowConfig) {
        super(workflowConfig);
    }

    public void startWorkflow() throws Exception {

        SparkConf sparkConf = workflowConfig.sparkConf();

        String workflowAppId = sparkConf.get("workflow.app.id");

        SparkSession spark = sparkSession();
        spark.sparkContext().setLogLevel("ERROR");

        Configuration hadoopConf = spark.sparkContext().hadoopConfiguration();
        System.out.println("sc.hadoopConfiguration() " + hadoopConf.toString());

        FileSystem fs = FileSystem.get(hadoopConf);
        System.out.println("fs.getHomeDirectory() " + fs.getHomeDirectory());
        System.out.println("fs.getWorkingDirectory() " + fs.getWorkingDirectory());
        System.out.println("fs.getClass.getCanonicalName) " + fs.getClass().getCanonicalName());
        System.out.println("s.getCanonicalServiceName " + fs.getCanonicalServiceName());

    }


}
