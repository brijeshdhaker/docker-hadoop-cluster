package org.examples.sb.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements Serializable {

    private Long id;
    private String name;
    private String status;
    public Long addTs;
    public Long updTs;

}
