package org.examples.sb.security;

import java.util.Collection;

import org.examples.sb.domain.Account;
import org.examples.sb.service.AccountService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter;
    private final String principalClaimName;
    private AccountService accountService;

    public CustomJwtAuthenticationConverter(AccountService accountService, Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter, String principalClaimName) {
        this.accountService = accountService;
        this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
        this.principalClaimName = principalClaimName != null ? principalClaimName : JwtClaimNames.SUB;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {

        Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(source);
        String principalClaimValue = source.getClaimAsString(this.principalClaimName);
        Account acc = accountService.findAccountByPrincipal(principalClaimValue);
        return new AccountToken(source, authorities, principalClaimValue, acc);
    }

}
