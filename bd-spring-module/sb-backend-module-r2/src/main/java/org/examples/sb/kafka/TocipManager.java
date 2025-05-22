package org.examples.sb.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class TocipManager {

    //@Bean
    public NewTopic transactionsTopic() {
        return TopicBuilder.name("transactions")
                .partitions(3)
                .replicas(1)
                .build();
    }

}
