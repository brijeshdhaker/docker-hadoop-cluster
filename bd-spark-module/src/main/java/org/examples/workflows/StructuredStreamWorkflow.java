package org.examples.workflows;

import org.apache.spark.sql.Row;
import org.examples.config.WorkflowConfig;

public class StructuredStreamWorkflow extends AbstractStreamWorkflow<String, byte[], Row> {

    public StructuredStreamWorkflow(WorkflowConfig workflowConfig) {
        super(workflowConfig);
    }
}
