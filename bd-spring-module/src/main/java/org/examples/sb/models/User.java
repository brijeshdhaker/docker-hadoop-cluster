package org.examples.sb.models;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {

    private Long id;
    private String name;
    private String email;
    private String status;
    protected LocalDateTime addTs;
    protected LocalDateTime updTs;

}
