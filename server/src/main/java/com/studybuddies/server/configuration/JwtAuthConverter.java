package com.studybuddies.server.configuration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

  @Override
  public AbstractAuthenticationToken convert(Jwt source) {
    Collection<GrantedAuthority> auths = Stream.concat(
            jwtGrantedAuthoritiesConverter.convert(source).stream(),
            extractRoles(source).stream())
        .collect(Collectors.toSet());

    return new JwtAuthenticationToken(source, auths);
  }

  private Collection<? extends GrantedAuthority> extractRoles(Jwt jwt) {
    Set<String> roles = new HashSet<>();

    // Extract roles from realm_access (if available)
    Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
    if (realmAccess != null && realmAccess.containsKey("roles")) {
      roles.addAll((Collection<? extends String>) realmAccess.get("roles"));
    }

    // Extract roles from resource_access dynamically
    Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
    if (resourceAccess != null) {
      for (Map.Entry<String, Object> entry : resourceAccess.entrySet()) {
        Map<String, Object> resource = (Map<String, Object>) entry.getValue();
        if (resource.containsKey("roles")) {
          roles.addAll((Collection<? extends String>) resource.get("roles"));
        }
      }
    }

    // Convert to Spring Security GrantedAuthorities with "ROLE_" prefix
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
        .collect(Collectors.toSet());
  }
}
