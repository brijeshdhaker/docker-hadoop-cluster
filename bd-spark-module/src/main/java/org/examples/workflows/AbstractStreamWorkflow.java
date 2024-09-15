package org.examples.workflows;

import org.examples.config.WorkflowConfig;

public abstract class AbstractStreamWorkflow<Key, Value, RddEntity> {

    protected WorkflowConfig workflowConfig;
    public AbstractStreamWorkflow(WorkflowConfig workflowConfig){

        this.workflowConfig = workflowConfig;

    }
}
