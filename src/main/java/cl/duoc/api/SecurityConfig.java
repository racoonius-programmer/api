package cl.duoc.api;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
        @Bean
        SecurityFilterChain security(HttpSecurity http) throws Exception {
                return http
                                .csrf(csrf -> csrf.disable())
                                .cors(Customizer.withDefaults())
                                .sessionManagement(s -> s.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))
.authorizeHttpRequests(auth -> auth
    // Agrupamos todas las rutas en una sola regla más limpia
    .requestMatchers(HttpMethod.GET, "/api/data", "/api/perfil", "/api/productos", "/api/productos/**").hasAuthority("SCOPE_access_as_user")
    // PERMITIMOS LA RUTA DE ERROR PARA VER QUÉ ESTÁ FALLANDO
    .requestMatchers("/error").permitAll()
    .anyRequest().denyAll()
)
                                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
                                .build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(
                                List.of("http://localhost:5173"));
                configuration.setAllowedMethods(
                                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(
                                List.of("Authorization", "Content-Type"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration(
                                "/**", configuration);
                return source;
        }
}