package org.examples.sb.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
//@ConfigurationProperties("kafka")
public class KafkaCustomProperties {
    private String transactionTopic;
}
