package org.examples.sb.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {

    private Long id;
    private Long sourceAccountId;
    private Long targetAccountId;
    private int amount;
    private String status;
    private Long groupId;


}
