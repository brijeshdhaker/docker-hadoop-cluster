package org.examples.workflows;

import org.apache.spark.sql.Row;
import org.examples.config.WorkflowConfig;

public class DiscretizedStreamWorkflow extends AbstractStreamWorkflow<String, byte[], Row> {

    public DiscretizedStreamWorkflow(WorkflowConfig workflowConfig) {
        super(workflowConfig);
    }
}
