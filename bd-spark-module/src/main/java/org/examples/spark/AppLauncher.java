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
import scala.remote;

import java.util.HashMap;
import java.util.Map;

public class AppLauncher {

    private static Options parseArgs(String[] args) throws ParseException {

        Options options = new Options();

        Option masterOption = new Option("m", "master", true, "Spark master");
        masterOption.setRequired(false);
        options.addOption(masterOption);

        Option bootstrapOption = new Option("b", "bootstrap-server", true, "Bootstrap servers");
        bootstrapOption.setRequired(true);
        options.addOption(bootstrapOption);

        Option workflowOption = new Option("w", "workflow-type", true, "Workflow Type");
        workflowOption.setRequired(true);
        options.addOption(workflowOption);
    
        Option topicOption = new Option("t", "topic", true, "Kafka topic");
        topicOption.setRequired(false);
        options.addOption(topicOption);
    
        Option schemaRegOption = new Option("s", "schema-registry", true, "Schema Registry URL");
        schemaRegOption.setRequired(false);
        options.addOption(schemaRegOption);

        Option helpOption = new Option("h", "help", false, "Print Help");
        helpOption.setRequired(false);
        options.addOption(helpOption);

        Option verboseOption = new Option("v", "verbose", false, "Verbose");
        verboseOption.setRequired(false);
        options.addOption(verboseOption);

        Option embeddedOption = new Option("e", "embedded", false, "Embedded");
        embeddedOption.setRequired(false);
        options.addOption(embeddedOption);

        //options.addOption("help", false, "Print Help");

        return options;
    }


    public static void main(String[] args) throws Exception {
        
        if(args.length > 0){
            
            
            HelpFormatter helper = new HelpFormatter();
            CommandLineParser parser = new BasicParser();
            CommandLine cmd = null;
            Options options = null;
            try {
                
                options = parseArgs(args);
                cmd = parser.parse(options, args);

                Map<String, String> params = new HashMap<>();
                for(Option a : cmd.getOptions()){
                    params.put(a.getLongOpt(), a.getValue());
                    params.put(a.getOpt(), a.getValue());
                }

                WorkflowConfig workflowConfig = new WorkflowConfig(params);

                if (args.length <= 0 || cmd.hasOption("help")) {

                    String opt_config = cmd.getOptionValue("config");
                    System.out.println("Config set to " + opt_config);

                }
                
                if(cmd.hasOption("workflow-type") && cmd.getOptionValue("workflow-type").equalsIgnoreCase("batch")) {

                    System.out.println("Spark Workflow Type :: Batch ");
                    BatchWorkflow flow = new BatchWorkflow(workflowConfig);

                }
            
                if (cmd.hasOption("workflow-type") && cmd.getOptionValue("workflow-type").equalsIgnoreCase("dstream")) {
                    System.out.println("Spark Workflow Type :: Discretized Stream ");
                    DiscretizedStreamWorkflow dstream = new DiscretizedStreamWorkflow(workflowConfig);
                }

                if (cmd.hasOption("workflow-type") && cmd.getOptionValue("workflow-type").equalsIgnoreCase("stream")) {
                    System.out.println("Spark Workflow Type :: Structured Stream ");
                    StructuredStreamWorkflow streamWorkflow = new StructuredStreamWorkflow(workflowConfig);
                }

            } catch (ParseException e) {

                System.out.println(e.getMessage());
                helper.printHelp("Usage : ", options);
                System.exit(0);
                
            }


        }
    }
}
