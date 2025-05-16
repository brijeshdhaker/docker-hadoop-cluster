package org.examples.sb.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;
import java.util.Map;

public class NamedOidcUser extends DefaultOidcUser {
    private static final long serialVersionUID = 1L;
    private final String userName;

    public NamedOidcUser(Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken,
                         OidcUserInfo userInfo, String userName) {
        super(authorities,idToken,userInfo);
        this.userName= userName;
    }

    @Override
    public String getName() {
        return this.userName;
    }

    public String getUserID() {
        Map<String, Object> claims = getClaims();
        return (String) claims.get("oid");
    }

    public String getClaimValue(String key) {
        Map<String, Object> claims = getClaims();
        return (String) claims.get(key );
    }
}
