package org.examples.config;

import org.apache.spark.SparkConf;
import org.examples.utils.PropertyFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.Map;

public abstract class AbstractAppConfig {


    private static final Logger logger = LoggerFactory.getLogger(AbstractAppConfig.class);

    private Boolean isVerbose;
    private Boolean isEmbedded;
    protected SparkConf sparkConf;
    private Map<String,String> params;

    public AbstractAppConfig(Map<String,String> params) throws Exception {
        this.params = params;
    }

    protected void init() throws Exception {

    }

    protected void loadWorkflowConfig(String path) throws Exception {
        Map<String,String> workflowParams = PropertyFileReader.loadFromFile(path);
        if(workflowParams != null && !workflowParams.isEmpty()){
            workflowParams.forEach(this.sparkConf::set);
        }
    }

    protected SparkConf embedded() throws Exception {

        SparkConf sparkConf = new SparkConf();
        if(Boolean.valueOf(this.params.get("")) || Boolean.valueOf(this.params.get(""))) {
            sparkConf.setMaster("local[4]");
            sparkConf.set("spark.submit.deployMode", "client");
            sparkConf.set("spark.sql.warehouse.dir", "");
            sparkConf.setMaster("local[4]");
        }

        if(Boolean.valueOf(this.params.get("--cassandra"))) {
            sparkConf.setMaster("local[4]");
            sparkConf.set("spark.submit.deployMode", "client");
            sparkConf.set("spark.sql.warehouse.dir", "");
            sparkConf.setMaster("local[4]");
        }
        
        return sparkConf;

    }


    private static String maskIfRequired(Tuple2<String, String> tuple) throws Exception {
        if(tuple._1().matches("(?i),*password.*|.*pwd.*|.*secret.*|.*authkey.*")){
            return tuple._1() + " : ***** ";
        }
        return tuple._1() + " : " + tuple._2();
    }
}
