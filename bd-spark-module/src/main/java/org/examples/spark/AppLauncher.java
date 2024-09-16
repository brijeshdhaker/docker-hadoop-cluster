package org.examples.spark;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import scala.remote;

public class AppLauncher {

    private static CommandLine parseArgs(String[] args) throws ParseException {

        Options options = new Options();

        Option masterOption = new Option("m", "master", true, "Spark master");
        masterOption.setRequired(false);
        options.addOption(masterOption);

        Option bootstrapOption = new Option("b", "bootstrap-server", true, "Bootstrap servers");
        bootstrapOption.setRequired(true);
        options.addOption(bootstrapOption);
    
        Option topicOption = new Option("t", "topic", true, "Kafka topic");
        topicOption.setRequired(true);
        options.addOption(topicOption);
    
        Option schemaRegOption = new Option("s", "schema-registry", true, "Schema Registry URL");
        schemaRegOption.setRequired(true);
        options.addOption(schemaRegOption);
    
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);

        return cmd;
    }


    public static void main(String[] args) {
        if(args.length > 0){

            
            HelpFormatter helper = new HelpFormatter();
            CommandLine cmd = null;
            try {

                cmd = parseArgs(args);
                if(cmd.hasOption("a")) {
                System.out.println("Alpha activated");
                }
            
                if (cmd.hasOption("c")) {
                String opt_config = cmd.getOptionValue("config");
                System.out.println("Config set to " + opt_config);
                }

            } catch (ParseException e) {

                System.out.println(e.getMessage());
                helper.printHelp("Usage:", options);
                System.exit(0);
                
            }


        }
    }
}
