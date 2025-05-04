package org.examples.sb.security;

import org.examples.sb.service.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@EnableWebSecurity
@Configuration
@EnableConfigurationProperties({JwtAuthorizationProperties.class,JwtMappingProperties.class})
public class SpringWebSecurityConfig {

    @Value("${spring.security.oauth2.client.provider.azure.issuer-uri}")
    private String issuer;

    @Value("${spring.security.oauth2.client.registration.azure-dev.client-id}")
    private String clientId;

    private final JwtMappingProperties mappingProps;
    private final AccountService accountService;

    public SpringWebSecurityConfig(JwtMappingProperties mappingProps, AccountService accountService) {
        this.mappingProps = mappingProps;
        this.accountService = accountService;
    }

    @Bean
    public String jwtGrantedAuthoritiesPrefix() {
        return mappingProps.getAuthoritiesPrefix();
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        MappingJwtGrantedAuthoritiesConverter converter = new MappingJwtGrantedAuthoritiesConverter(mappingProps.getScopes());

        if (StringUtils.hasText(mappingProps.getAuthoritiesPrefix())) {
            converter.setAuthorityPrefix(mappingProps.getAuthoritiesPrefix()
                    .trim());
        }

        if (StringUtils.hasText(mappingProps.getAuthoritiesClaimName())) {
            converter.setAuthoritiesClaimName(mappingProps.getAuthoritiesClaimName());
        }
        return converter;
    }

    @Bean
    public Converter<Jwt,AbstractAuthenticationToken> customJwtAuthenticationConverter(AccountService accountService) {
        return new CustomJwtAuthenticationConverter(
                accountService,
                jwtGrantedAuthoritiesConverter(),
                mappingProps.getPrincipalClaimName());
    }

    /*
    @Bean
    SecurityFilterChain customJwtSecurityChain(HttpSecurity http) throws Exception {
        // @formatter:off
        return http.oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(customJwtAuthenticationConverter(accountService))))
                .build();
        // @formatter:on
    }
    */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthorizationProperties jwtprops) throws Exception {

        // @formatter:off
        http.authorizeHttpRequests((requests) -> requests
                // the / and /home paths are configured to not require any authentication.
                //.requestMatchers("/", "/home").permitAll()
                .requestMatchers("/", "/images/**").permitAll()
                // All other paths must be authenticated.
                .anyRequest().authenticated())
        .headers((headers) -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
        .oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                        .oidcUserService(customOidcUserService(jwtprops))))
        //.logout(Customizer.withDefaults())
        .logout(logout -> logout
                .deleteCookies()
                .logoutSuccessUrl("http://localhost:8080/")
                .invalidateHttpSession(true)
                //.addLogoutHandler(logoutHandler())
        )
        .csrf(AbstractHttpConfigurer::disable);

        // @formatter:on
        return http.build();
    }

    private LogoutHandler logoutHandler() {
        return (request, response, authentication) -> {
            try {
                //
                String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
                response.sendRedirect( "https://login.microsoftonline.com/common/oauth2/v2.0/logout?client_id=" + clientId + "&returnTo=" + baseUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /*
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
    */

    private OAuth2UserService<OidcUserRequest, OidcUser> customOidcUserService(JwtAuthorizationProperties props) {
        final OidcUserService oidcUserService = new OidcUserService();
        final GroupsClaimMapper mapper = new GroupsClaimMapper(
                props.getAuthoritiesPrefix(),
                props.getGroupsClaim(),
                props.getGroupToAuthorities());

        return userRequest -> {
            OidcUser oidcUser = oidcUserService.loadUser(userRequest);
            // Enrich standard authorities with groups
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            mappedAuthorities.addAll(oidcUser.getAuthorities());
            mappedAuthorities.addAll(mapper.mapAuthorities(oidcUser));

            oidcUser = new NamedOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo(),oidcUser.getName());

            return oidcUser;
        };
    }

    @Bean
    @Profile("oauth2-extractors-sandbox")
    public PrincipalExtractor sandboxPrincipalExtractor() {
        return new SandboxPrincipalExtractor();
    }

    @Bean
    @Profile("oauth2-extractors-sandbox")
    public AuthoritiesExtractor sandboxAuthoritiesExtractor() {
        return new SandboxAuthoritiesExtractor();
    }


}
