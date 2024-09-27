package org.examples.workflows;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.examples.config.WorkflowConfig;

public class Workflow {

    protected WorkflowConfig workflowConfig;
    private SparkSession session = null;

    public Workflow(WorkflowConfig workflowConfig){
        this.workflowConfig = workflowConfig;
    }

    protected SparkSession sparkSession() {

        if(this.session != null){
            return this.session;
        }

        if(this.workflowConfig != null) {

            SparkConf sparkConf = workflowConfig.sparkConf();
            String currentJobName = sparkConf.get("workflow.app.name");

            this.session = SparkSession
                    .builder()
                    .master("local[*]")  // // spark://spark-master.sandbox.net:7077")
                    .appName(currentJobName)
                    .config(sparkConf)
                    .getOrCreate();

        }

        return this.session;
    }

}
