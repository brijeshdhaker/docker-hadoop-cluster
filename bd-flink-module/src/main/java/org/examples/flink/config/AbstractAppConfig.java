package org.examples.flink.config;

import org.apache.flink.core.fs.FileSystem;
import org.examples.flink.utils.Constants;
import org.examples.flink.utils.PropertyFileReader;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.net.URI;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class AbstractAppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAppConfig.class);

    private Boolean isVerbose;
    private Boolean isEmbedded;

    private Map<String,String> params = new LinkedHashMap<>();

    public AbstractAppConfig(String[] args) throws Exception {

        ParameterTool arg_params = ParameterTool.fromArgs(args);
        Map<String, String> args_map = arg_params.toMap();
        args_map.forEach((key, value) -> params.put(key.toString(), value.toString()));

        String isVerbose  = args_map.getOrDefault("verbose", "true");
        params.put("verbose", isVerbose);

        String engine_type  = args_map.getOrDefault(Constants.ENGINE_TYPE, Constants.LOCAL_CLUSTER);
        params.put(Constants.ENGINE_TYPE, engine_type);

        String uuid = UUID.randomUUID().toString().split("-")[0];
        String workflow_id  = args_map.getOrDefault(Constants.WORKFLOW_ID, uuid);
        params.put(Constants.WORKFLOW_ID,workflow_id);

        String workflow_name  = args_map.getOrDefault(Constants.WORKFLOW_NAME, "Workflow");
        params.put(Constants.WORKFLOW_NAME,workflow_name+"-"+uuid);

        init();
    }

    protected void init() throws Exception {

        isVerbose = Boolean.valueOf(params.get("verbose"));
        isEmbedded = Boolean.valueOf(params.get("embedded"));

        if(!params.isEmpty()){

            String workflowConfigPath = params.get(Constants.APP_CONFIG_PATH);
            String workflowMarkerPath = params.get(Constants.APP_MARKER_PATH);

            if(isVerbose){
                System.out.println("Workflow config file : " + workflowConfigPath);
                System.out.println("Workflow marker file : " + workflowMarkerPath);
            }

            if(isEmbedded){
                embedded();
            }

            if(workflowConfigPath != null && !workflowConfigPath.isEmpty()){
                loadWorkflowConfig(workflowConfigPath);
            }

            if(workflowMarkerPath != null && !workflowMarkerPath.isEmpty()){
                //this.flinkConf.set("workflow.marker.file", workflowMarkerPath);
            }

            String summary = params.entrySet().stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(e ->{
                        return new Tuple2<>(e.getKey(), e.getValue());
                    })
                    .map(AbstractAppConfig::maskIfRequired)
                    .collect(Collectors.joining(System.lineSeparator()));


            logger.info("flink configuration summary : {}", summary);

        }
    }

    protected void loadWorkflowConfig(String path) throws Exception {

        Map<String,String> workflowParams = PropertyFileReader.loadFromDefaultFS(path);
        if(!workflowParams.isEmpty()){
            workflowParams.forEach(this.params::put);
        }

    }

    public Map<String,String> workflowConf(){
        return params;
    }

    public Configuration flinkConf(){
        String engine_type = params.get(Constants.ENGINE_TYPE);
        String config_dir = params.getOrDefault(Constants.FLINK_CONFIG_PATH, "/opt/flink/conf");
        return engine_type.equalsIgnoreCase(Constants.REMOTE_CLUSTER) ?
                GlobalConfiguration.loadConfiguration() :
                GlobalConfiguration.loadConfiguration(config_dir);

    }

    protected void embedded() throws Exception {

        if(Boolean.parseBoolean(this.params.get("cassandra"))) {
            params.put("cassandra.connection.host", "cassandra-node01.sandbox.net");
        }

        //
        if(this.params.get("storage") != null && this.params.get("storage").equalsIgnoreCase("s3")) {

            params.put("flink.hadoop.fs.defaultFS", "s3a://defaultfs/");
            params.put("flink.hadoop.fs.s3a.endpoint", "http://minio.sandbox.net:9010");
            params.put("flink.hadoop.fs.s3a.access.key", "pgm2H2bR7a5kMc5XCYdO");
            params.put("flink.hadoop.fs.s3a.secret.key", "zjd8T0hXFGtfemVQ6AH3yBAPASJNXNbVSx5iddqG");
            params.put("flink.hadoop.fs.s3a.path.style.access", "true");
            params.put("flink.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider");
            params.put("flink.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");

        }

        //
        if(this.params.get("storage") != null && this.params.get("storage").equalsIgnoreCase("adls")) {

            params.put("flink.hadoop.fs.defaultFS", "abfs://warehouse@devstoreaccount1");
            params.put("flink.hadoop.fs.azure.account.key.devstoreaccount1.blob.core.windows.net", "Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==");
            params.put("flink.hadoop.fs.azure.storage.emulator.account.name", "devstoreaccount1");

        }

    }


    private static String maskIfRequired(Tuple2<String, String> tuple) {
        String[] SENSITIVE_KEYS = new String[]{"password", "access.key", "secret", "fs.azure.account.key", "apikey", "auth-params", "service-key", "token", "basic-auth", "jaas.config", "http-headers"};
        for(String hideKey : SENSITIVE_KEYS) {
            if (tuple._1().length() >= hideKey.length() && tuple._1().contains(hideKey)) {
                return tuple._1() + " : ***** ";
            }
        }
        //if(tuple._1().matches("(?i),*password.*|.*pwd.*|.*secret.*|.*access.key|.*authkey.*")){
            //return tuple._1() + " : ***** ";
        //}
        return tuple._1() + " : " + tuple._2();
    }
}
