package com.pruebaTecnica.msInventario.config; // 1. Paquete corregido para msInventario

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Activar CORS usando la configuración del bean de abajo
                .cors(withDefaults())

                // Desactivar CSRF ya que es una API pública
                .csrf(csrf -> csrf.disable())

                // 2. Configurar las reglas de autorización
                .authorizeHttpRequests(auth -> auth
                        // Permite todas las peticiones a cualquier ruta sin autenticación
                        .anyRequest().permitAll());

        return http.build();
    }

    // para que el navegador permita la llamada desde el frontend.
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permitir explícitamente el origen del frontend
        configuration.setAllowedOrigins(List.of("http://localhost:93"));

        // Permitir los métodos HTTP necesarios
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE", "OPTIONS"));

        // Permitir todas las cabeceras
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplicar esta configuración a TODAS las rutas de la API ("/**")
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
