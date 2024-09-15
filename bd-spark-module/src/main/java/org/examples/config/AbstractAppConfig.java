package org.examples.config;

import java.util.Map;

public abstract class AbstractAppConfig {

    private Boolean isVerbose;
    private Boolean isEmbedded;
    private Map<String,String> params;

    public AbstractAppConfig(Map<String,String> params) throws Exception {
        this.params = params;






        
    }
}
