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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // @formatter:off
        http.authorizeHttpRequests((requests) -> requests
                // All API URLs must be authenticated.
                .requestMatchers("/api/v1/**").authenticated()
                // All other paths allowed without auth.
                .anyRequest().permitAll())
        .headers((headers) -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
        .formLogin(AbstractHttpConfigurer::disable)
        .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                .jwt(jwt -> jwt
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        .jwkSetUri(jwkSetUri)
                ))
        .httpBasic(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable);

        // @formatter:on
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("admin")
                .password("password")
                .roles("ACTUATOR")
                .build();
        return new InMemoryUserDetailsManager(user);
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
