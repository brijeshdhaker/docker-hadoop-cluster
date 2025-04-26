package org.examples.sb.security;

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
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@Configuration
@EnableConfigurationProperties(JwtAuthorizationProperties.class)
public class SpringSecurityConfig {


    @Value("${spring.security.oauth2.resourceserver.jwk.jwk-set-uri}")
    protected String jwkSetUri;

    @Value("${spring.security.oauth2.resourceserver.id}")
    protected String apiUri;


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthorizationProperties jwtprops) throws Exception {

        // @formatter:off
        http.authorizeHttpRequests((requests) -> requests
                /*
                // the /actuator/** paths are configured to not require any authentication.
                .requestMatchers(antMatcher("/actuator/**")).permitAll()
                // the /h2-console/** paths are configured to not require any authentication.
                .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                // the /api-docs/** & /swagger-ui/** paths are configured to not require any authentication.
                .requestMatchers(antMatcher("/api-docs/**")).permitAll()
                .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()
                // the / and /home paths are configured to not require any authentication.
                .requestMatchers("/", "/home").permitAll()
                */
                // All API URLs must be authenticated.
                .requestMatchers("/api/v1/**").authenticated()
                // All other paths must be authenticated.
                .anyRequest().permitAll())
        .headers((headers) -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
        //.formLogin((form) -> form.loginPage("/login").permitAll()
        .formLogin(AbstractHttpConfigurer::disable)
        // .oauth2Login(Customizer.withDefaults())
        .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.oidcUserService(customOidcUserService(jwtprops))))
        .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                .jwt(jwt -> jwt
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        .jwkSetUri(jwkSetUri)
                ))
        .logout(LogoutConfigurer::permitAll);

        // @formatter:on
        return http.build();
    }

    /*
    @Bean
    SecurityFilterChain customJwtSecurityChain(HttpSecurity http, JwtAuthorizationProperties props) throws Exception {
        // @formatter:off
        return http
                .authorizeHttpRequests( r -> r.anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(ep ->
                        ep.oidcUserService(customOidcUserService(props))))
                .build();
        // @formatter:on
    }


    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }


    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return  userRequest -> {
            OidcUser oidcUser = new OidcUserService().loadUser(userRequest);
            // add any custom user service integration here , if required
            return oidcUser;
        };
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



    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        //grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
