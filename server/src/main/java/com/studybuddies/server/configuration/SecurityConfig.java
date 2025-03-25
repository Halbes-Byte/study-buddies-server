package com.studybuddies.server.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

  private JwtAuthConverter jwtAuthConverter;
  private CorsConfigurationSource corsConfigurationSource;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/**");
    http.cors(c -> c.configurationSource(corsConfigurationSource));
    http.
        authorizeHttpRequests(auth ->
            auth.requestMatchers("/swagger-ui/**", "/h2-console/**", "/api-docs/**", "/v3/**").permitAll()
        )
        .oauth2ResourceServer(oauth2 ->
            oauth2.jwt(jwt ->
                jwt.jwtAuthenticationConverter(jwtAuthConverter)
            )
        );
    http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
    return http.build();
  }
}
