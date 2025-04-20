package org.examples.sb.models;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> roles = new ArrayList<>();
    public Long addTs;
    public Long updTs;

    public void addRoles(String role) {
        this.roles.add(role);
    }

}
