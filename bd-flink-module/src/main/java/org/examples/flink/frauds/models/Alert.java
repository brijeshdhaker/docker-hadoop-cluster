package org.examples.flink.frauds.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {
    private String customerId;
    private AlertType alertType;
    private String message;
}
