package com.studybuddies.server.configuration;

import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CORS {

  @Bean
    public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration conf = new CorsConfiguration();
    conf.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:7070"));
    conf.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    conf.setAllowedHeaders(List.of("*"));
    conf.setAllowCredentials(true);
    conf.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", conf);
    return source;
  }
}
