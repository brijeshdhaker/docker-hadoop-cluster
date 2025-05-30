package org.examples.sb.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix="spring.security.oauth2.resourceserver.jwt.authorization")
public class JwtAuthorizationProperties {

    // Claim that has the group list
    private String groupsClaim = "groups";

    private String authoritiesPrefix = "SCOPE_";

    // map groupIds to a list of authorities.
    private Map<String, List<String>> groupToAuthorities = new HashMap<>();

    /**
     * @return the groupsClaim
     */
    public String getGroupsClaim() {
        return groupsClaim;
    }

    /**
     * @param groupsClaim the groupsClaim to set
     */
    public void setGroupsClaim(String groupsClaim) {
        this.groupsClaim = groupsClaim;
    }

    /**
     * @return the groupToAuthorities
     */
    public Map<String, List<String>> getGroupToAuthorities() {
        return groupToAuthorities;
    }

    /**
     * @param groupToAuthorities the groupToAuthorities to set
     */
    public void setGroupToAuthorities(Map<String, List<String>> groupToAuthorities) {
        this.groupToAuthorities = groupToAuthorities;
    }

    /**
     * @return the authoritiesPrefix
     */
    public String getAuthoritiesPrefix() {
        return authoritiesPrefix;
    }

    /**
     * @param authoritiesPrefix the authoritiesPrefix to set
     */
    public void setAuthoritiesPrefix(String authoritiesPrefix) {
        this.authoritiesPrefix = authoritiesPrefix;
    }
}
