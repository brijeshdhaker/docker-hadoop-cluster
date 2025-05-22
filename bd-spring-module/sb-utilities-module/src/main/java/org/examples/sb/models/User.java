package org.examples.sb.models;

import lombok.*;

import java.io.Serializable;
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
    private List<Role> roles = new ArrayList<>();
    public Long addTs;
    public Long updTs;

    public void addRoles(Role role) {
        this.roles.add(role);
    }

}
