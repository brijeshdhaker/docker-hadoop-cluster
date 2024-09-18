package org.examples.config;

import org.apache.spark.SparkConf;
import org.examples.utils.PropertyFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractAppConfig {


    private static final Logger logger = LoggerFactory.getLogger(AbstractAppConfig.class);

    private Boolean isVerbose;
    private Boolean isEmbedded;
    protected SparkConf sparkConf;
    private Map<String,String> params;

    public AbstractAppConfig(Map<String,String> params) throws Exception {
        this.params = params;
        init();
    }
    
    protected void init() throws Exception {

        isVerbose = Boolean.valueOf(params.get("verbose"));
        isEmbedded = Boolean.valueOf(params.get("embedded"));

        if(!params.isEmpty()){
            
            String workflowConfigPath = params.get("property-file");
            String workflowMarkerPath = params.get("marker-file");

            if(isVerbose){
                System.out.println("Workflow property-file : " + workflowConfigPath);
                System.out.println("Workflow marker-file   : " + workflowMarkerPath);
            }

            this.sparkConf = isEmbedded ? embedded() : new SparkConf();

            if(workflowConfigPath != null && !workflowConfigPath.isEmpty()){
                loadWorkflowConfig(workflowConfigPath);
            }

            if(workflowMarkerPath != null && !workflowMarkerPath.isEmpty()){
               this.sparkConf.set("workflow.marker.file", workflowMarkerPath);
            }

            String summary = Stream.of(sparkConf.getAll())
                    .sorted(Comparator.comparing(Tuple2::_1))
                    .map(AbstractAppConfig::maskIfRequired)
                    .collect(Collectors.joining(System.lineSeparator()));


            logger.info("Sprak configuration summary : {}", summary);

        }
    }

    protected void loadWorkflowConfig(String path) throws Exception {
        
        Map<String,String> workflowParams = PropertyFileReader.loadFromFile(path);
        if(workflowParams != null && !workflowParams.isEmpty()){
            workflowParams.forEach(this.sparkConf::set);
        }
        
    }

    public SparkConf sparkConf(){
        
        return sparkConf;
        
    }

    protected SparkConf embedded() throws Exception {

        SparkConf sparkConf = new SparkConf();
        
        sparkConf.setMaster("local[4]");
        sparkConf.set("spark.submit.deployMode", "client");
        sparkConf.set("spark.sql.warehouse.dir", "/apps/sandbox/warehouse");
        sparkConf.set("spark.eventLog.enabled", "true");
        sparkConf.set("spark.jars.ivy", "/apps/.ivy2");
        sparkConf.set("spark.eventLog.dir", "/apps/var/logs/spark-events");
        
        if(Boolean.parseBoolean(this.params.get("cassandra"))) {
            sparkConf.set("spark.cassandra.connection.host", "127.0.0.1");
        }
        
        //
        if(this.params.get("storage") != null && this.params.get("storage").equalsIgnoreCase("s3")) {
            
            sparkConf.set("spark.hadoop.fs.s3a.endpoint", "http://minio.sandbox.net:9010");
            sparkConf.set("spark.hadoop.fs.s3a.access.key", "pgm2H2bR7a5kMc5XCYdO");
            sparkConf.set("spark.hadoop.fs.s3a.secret.key", "zjd8T0hXFGtfemVQ6AH3yBAPASJNXNbVSx5iddqG");
            sparkConf.set("spark.hadoop.fs.s3a.path.style.access", "true");
            sparkConf.set("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");
            sparkConf.set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
            
        }

        //
        if(this.params.get("storage") != null && this.params.get("storage").equalsIgnoreCase("adls")) {

            sparkConf.set("spark.hadoop.fs.s3a.endpoint", "http://minio.sandbox.net:9010");
            sparkConf.set("spark.hadoop.fs.s3a.access.key", "pgm2H2bR7a5kMc5XCYdO");
            sparkConf.set("spark.hadoop.fs.s3a.secret.key", "zjd8T0hXFGtfemVQ6AH3yBAPASJNXNbVSx5iddqG");
            sparkConf.set("spark.hadoop.fs.s3a.path.style.access", "true");
            sparkConf.set("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");
            sparkConf.set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");

        }

        return sparkConf;

    }


    private static String maskIfRequired(Tuple2<String, String> tuple) {
        
        if(tuple._1().matches("(?i),*password.*|.*pwd.*|.*secret.*|.*access.key|.*authkey.*")){
            return tuple._1() + " : ***** ";
        }
        return tuple._1() + " : " + tuple._2();
    }
    
}
