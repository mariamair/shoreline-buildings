package com.buildings.config;

import jakarta.servlet.http.HttpServletResponse;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Value("${ACCESS_TOKEN_PUBLIC_KEY}")
  private String base64PublicKey;

  @Bean
  public RSAPublicKey rsaPublicKey() throws Exception {
    byte[] decoded = Base64.getDecoder().decode(base64PublicKey);
    // Strip the PEM headers and decode
    String pem = new String(decoded)
      .replace("-----BEGIN PUBLIC KEY-----", "")
      .replace("-----END PUBLIC KEY-----", "")
      .replaceAll("\\s", "");
    byte[] keyBytes = Base64.getDecoder().decode(pem);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
    return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
  }

  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http, final RSAPublicKey rsaPublicKey) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .anyRequest().permitAll() // GraphQL endpoint open, handled at resolver level
      )
      .oauth2ResourceServer(oauth2 -> oauth2
        .jwt(jwt -> jwt
          .decoder(NimbusJwtDecoder.withPublicKey(rsaPublicKey).build())
        )
        .authenticationEntryPoint(authenticationEntryPoint())
        .accessDeniedHandler(accessDeniedHandler())
      );
    return http.build();
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    return (request, response, authException) -> {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("""
        {
          "errors": [{
            "message": "Authentication required.",
            "extensions": { "code": "UNAUTHORIZED", "classification": "AuthorizationError" }
          }]
        }
      """);
    };
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return (request, response, accessDeniedException) -> {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("application/json");
      response.getWriter().write("""
        {
          "errors": [{
            "message": "You do not have permission to perform this action.",
            "extensions": {  "code": "FORBIDDEN", "classification": "AuthorizationError" }
          }]
        }
    """);
    };
  }
}
