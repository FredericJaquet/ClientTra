package com.frederic.clienttra.config;

import com.frederic.clienttra.security.CustomAccessDeniedHandler;
import com.frederic.clienttra.security.CustomUserDetailsService;
import com.frederic.clienttra.security.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuration class for Spring Security settings.
 * <p>
 * This class defines password encoding, authentication manager, and security filter chain
 * for request authorization, form login, logout, and exception handling.
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Defines the password encoder bean using BCrypt hashing algorithm.
     *
     * @return a {@link PasswordEncoder} instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configures the security filter chain for HTTP security:
     * <ul>
     *     <li>Disables CSRF (temporarily, until the frontend is integrated)</li>
     *     <li>Allows unauthenticated access to log in, log out, and static resources</li>
     *     <li>Requires authentication for all other endpoints</li>
     *     <li>Enables default form login</li>
     *     <li>Configures logout behavior and session cleanup</li>
     *     <li>Sets custom access denied handler</li>
     * </ul>
     *
     * @param http                 the {@link HttpSecurity} builder
     * @param accessDeniedHandler  the custom handler for access denied exceptions
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception in case of misconfiguration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CustomAccessDeniedHandler accessDeniedHandler) throws Exception {

        http
            // CSRF is disabled until the frontend is implemented (e.g., React)
            .csrf(csrf -> csrf.disable())
            .cors(withDefaults())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Define public endpoints and require authentication for all others
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/auth/login",
                    "/api/registration/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/css/**", "/js/**", "/favicon.ico",
                    "/error"
                ).permitAll()
                .anyRequest().authenticated()
            )

            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

            // Configure logout behavior
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout") // Can be replaced with JSON for API use
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )

            // Use custom access denied handler for forbidden requests
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"error\":\"Unauthorized\"}");
                        })
                        .accessDeniedHandler(accessDeniedHandler)
                );

        return http.build();
    }

    /**
     * Define CORS configuration for all endpoints.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // permite cualquier origen en desarrollo; en producción puedes restringir
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // importante para cookies de sesión

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
