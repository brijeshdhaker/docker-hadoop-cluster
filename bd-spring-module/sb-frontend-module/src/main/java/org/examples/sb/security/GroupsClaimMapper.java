package org.examples.sb.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.ClaimAccessor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Baeldung
 *
 */
public class GroupsClaimMapper  {

    private final String authoritiesPrefix;
    private final String groupsClaim;
    private final Map<String, List<String>> groupToAuthorities;

    public GroupsClaimMapper(String authoritiesPrefix, String groupsClaim, Map<String, List<String>> groupToAuthorities) {
        this.authoritiesPrefix = authoritiesPrefix;
        this.groupsClaim = groupsClaim;
        this.groupToAuthorities = Collections.unmodifiableMap(groupToAuthorities);
    }

    public Collection<? extends GrantedAuthority> mapAuthorities(ClaimAccessor source) {

        List<String> groups = source.getClaimAsStringList(groupsClaim);
        if ( groups == null || groups.isEmpty()) {
            return Collections.emptyList();
        }

        List<GrantedAuthority> result = new ArrayList<>();
        for( String g : groups) {
            List<String> authNames = groupToAuthorities.get(g);
            if ( authNames == null ) {
                continue;
            }

            List<SimpleGrantedAuthority> mapped = authNames.stream()
                    .map( s -> authoritiesPrefix + s)
                    .map( SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            result.addAll(mapped);
        }

        return result;
    }

}
