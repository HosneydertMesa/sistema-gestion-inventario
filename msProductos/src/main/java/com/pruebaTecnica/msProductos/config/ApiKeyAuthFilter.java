package com.pruebaTecnica.msProductos.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Value("${security.api-key.secret}")
    private String secretApiKey;

    private static final List<String> SWAGGER_PATHS = List.of(
            "/swagger-ui.html",
            "/swagger-ui",
            "/v3/api-docs");

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Verificar si la ruta es pública (Swagger) o de pre-vuelo (OPTIONS)
        String path = request.getRequestURI();
        boolean isPublicPath = SWAGGER_PATHS.stream().anyMatch(path::startsWith);

        if (isPublicPath || HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Obtener la API Key de la petición
        String requestApiKey = request.getHeader("X-API-KEY");

        // 3. Validar la API Key
        if (secretApiKey.equals(requestApiKey)) {
            // 4. Si es válida, crear el objeto de autenticación y lo registramos
            var authentication = new UsernamePasswordAuthenticationToken(
                    "api-user", null, Collections.singletonList(() -> "ROLE_API_USER"));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Continuamos con la cadena de filtros
            filterChain.doFilter(request, response);
        } else {
            // 5. Si no es válida, devolver un error
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("API Key Inválida o Faltante");
        }
    }
}
