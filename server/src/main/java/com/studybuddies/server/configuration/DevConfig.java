package com.studybuddies.server.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Profile("dev")
@Configuration
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class DevConfig {

  @Qualifier("cors")
  private CorsConfigurationSource corsConfigurationSource;

  @Bean
  public SecurityFilterChain devChain(HttpSecurity http) throws Exception {
    log.info("Dev configuring SecurityFilterChain");
    http.cors(c -> c.configurationSource(corsConfigurationSource));
    http.securityMatcher("/**");
    http.csrf(CsrfConfigurer::disable);
    http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
    return http.build();
  }
}
