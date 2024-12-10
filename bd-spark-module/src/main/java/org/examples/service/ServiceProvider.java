package org.examples.service;

import org.apache.spark.SparkConf;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Query;
import org.codejargon.fluentjdbc.api.query.Transaction;
import org.codejargon.fluentjdbc.api.query.listen.AfterQueryListener;
import org.examples.datasource.DatasourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.List;

public class ServiceProvider {


    private static final Logger logger = LoggerFactory.getLogger(ServiceProvider.class);

    private SparkConf sparkConf;
    private Query query;
    private TopicService topicService;
    private OffsetService offsetService;

    private static ServiceProvider instance;

    public ServiceProvider(SparkConf sparkConf) {
        this.sparkConf = sparkConf;
    }

    public static synchronized  ServiceProvider getInstance(SparkConf sparkConf) {
        if(instance == null){
            instance = new ServiceProvider(sparkConf);
        }
        return instance;
    }

    protected Query query(){
        if(query != null){
            return query;
        }

        DataSource dataSource = DatasourceBuilder.as()
                .url(sparkConf.get("workflow.database.url"))
                .user(sparkConf.get("workflow.database.user"))
                .password(sparkConf.get("workflow.database.password"))
                .build();

        query = new FluentJdbcBuilder().connectionProvider(dataSource)
                .defaultTransactionIsolation(Transaction.Isolation.READ_COMMITTED)
                .afterQueryListener(listener()).build().query();

        return query;

    }


    private static AfterQueryListener listener(){
        return  execution -> {
           if(execution.success()){
               logger.info("Query took {} ms to execute: {}", execution.executionTimeMs(), execution.sql());
           }
           execution.sqlException().ifPresent(e -> logger.error("SQL Error ", e));
        } ;
    }



    public OffsetService offsetService(){
        if(offsetService == null){
            offsetService = new OffsetService(query());
        }
        return offsetService;
    }

    public TopicService topicService(List<String> topics){
        if(topicService == null){
            topicService = new TopicService(query(), topics);
        }
        return topicService;
    }
}
