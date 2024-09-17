package org.examples.workflows;

import org.apache.spark.SparkConf;
import org.examples.config.WorkflowConfig;

public class BatchWorkflow {

    private WorkflowConfig workflowConfig;


    public BatchWorkflow(WorkflowConfig workflowConfig) {
        this.workflowConfig = workflowConfig;
    }

    public void start() {
        if(this.workflowConfig != null){
            SparkConf sparkConf = workflowConfig.sparkConf();

        }
    }

}
