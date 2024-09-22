/**

mvn exec:exec@run-local -Drunclass=com.spark.streaming.structure.KafkaStructuredStream -Dparams="50"

--packages org.apache.spark:spark-avro_2.12:3.4.1,org.apache.spark:spark-streaming-kafka-0-10_2.12:3.4.1,org.apache.spark:spark-sql-kafka-0-10_2.12:3.4.1,org.apache.spark:spark-hadoop-cloud_2.12:3.4.1 \

$SPARK_HOME/bin/spark-submit \
--name "AppLauncher::BatchWorkflow" \
--master local[4] \
--conf spark.jars.packages=org.apache.spark:spark-avro_2.12:3.4.1,org.apache.spark:spark-streaming-kafka-0-10_2.12:3.4.1,org.apache.spark:spark-sql-kafka-0-10_2.12:3.4.1,org.apache.spark:spark-hadoop-cloud_2.12:3.4.1 \
--conf spark.jars.ivy=/apps/.ivy2 \
--class org.examples.spark.AppLauncher \
bd-spark-module/target/bd-spark-module-1.0-SNAPSHOT.jar --embedded --verbose --workflow-type batch --storage local --property-file /home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-spark-module/src/main/resources/app_workflow.properties --marker-file /home/brijeshdhaker/IdeaProjects/docker-hadoop-cluster/bd-spark-module/src/main/resources/app_workflow.marker

*/


package org.examples.spark;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import org.examples.config.WorkflowConfig;
import org.examples.workflows.BatchWorkflow;
import org.examples.workflows.DiscretizedStreamWorkflow;
import org.examples.workflows.StructuredStreamWorkflow;

import java.util.HashMap;
import java.util.Map;

public class AppLauncher {

    private static Options parseArgs(String[] args) throws ParseException {

        Options options = new Options();

        Option workflowOption = new Option("w", "workflow-type", true, "Workflow Type");
        workflowOption.setRequired(true);
        options.addOption(workflowOption);

        Option storageOption = new Option("s", "storage", true, "Storage Type");
        storageOption.setRequired(false);
        options.addOption(storageOption);

    
        Option helpOption = new Option("h", "help", false, "Print Help");
        helpOption.setRequired(false);
        options.addOption(helpOption);

        Option verboseOption = new Option("v", "verbose", false, "Verbose");
        verboseOption.setRequired(false);
        options.addOption(verboseOption);

        Option embeddedOption = new Option("e", "embedded", false, "Embedded");
        embeddedOption.setRequired(false);
        options.addOption(embeddedOption);

        Option propertyOption = new Option("p", "property-file", true, "Embedded");
        propertyOption.setRequired(true);
        options.addOption(propertyOption);
        
        Option markerOption = new Option("m", "marker-file", true, "Embedded");
        markerOption.setRequired(false);
        options.addOption(markerOption);
        
        //options.addOption("help", false, "Print Help");

        return options;
    }



    public static void main(String[] args) throws Exception {
        
        if(args.length > 0){
            
            
            HelpFormatter helper = new HelpFormatter();
            CommandLineParser parser = new BasicParser();
            Options options = parseArgs(args);;
             
            try {
                
                
                CommandLine cli = parser.parse(options, args);
                Map<String, String> params = new HashMap<>();
                for(Option a : cli.getOptions()){
                    params.put(a.getLongOpt(), a.getValue());
                    params.put(a.getOpt(), a.getValue());
                }
                
                if(cli.hasOption("embedded")){
                    params.put("embedded", "true");
                    params.put("e", "true");
                }
                
                if(cli.hasOption("verbose")){
                    params.put("verbose", "true");
                    params.put("v", "true");
                }
                
                
                WorkflowConfig workflowConfig = new WorkflowConfig(params);

                if (args.length <= 0 || cli.hasOption("help")) {

                    String workflow_type = cli.getOptionValue("workflow-type");
                    helper.printHelp("Usage : ", options);

                }
                
                if(cli.hasOption("workflow-type") && cli.getOptionValue("workflow-type").equalsIgnoreCase("batch")) {

                    System.out.println("Spark Workflow Type :: Batch ");
                    BatchWorkflow flow = new BatchWorkflow(workflowConfig);
                    flow.startWorkflow();

                }
            
                if (cli.hasOption("workflow-type") && cli.getOptionValue("workflow-type").equalsIgnoreCase("dstream")) {
                    
                    System.out.println("Spark Workflow Type :: Discretized Stream ");
                    DiscretizedStreamWorkflow dstream = new DiscretizedStreamWorkflow(workflowConfig);
                    dstream.startWorkflow();
                }

                if (cli.hasOption("workflow-type") && cli.getOptionValue("workflow-type").equalsIgnoreCase("stream")) {
                    
                    System.out.println("Spark Workflow Type :: Structured Stream ");
                    StructuredStreamWorkflow streamWorkflow = new StructuredStreamWorkflow(workflowConfig);
                    streamWorkflow.startWorkflow();

                }

            } catch (ParseException e) {

                System.out.println(e.getMessage());
                helper.printHelp("Usage : ", options);
                System.exit(0);
                
            }


        }
    }
}
