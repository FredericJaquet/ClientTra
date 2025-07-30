package com.frederic.clienttra.config;

import com.frederic.clienttra.security.CustomAccessDeniedHandler;
import com.frederic.clienttra.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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

    private final CustomUserDetailsService userDetailsService;

    /**
     * Defines the password encoder bean using BCrypt hashing algorithm.
     *
     * @return a {@link PasswordEncoder} instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the authentication manager with a DAO-based provider
     * using the custom {@link CustomUserDetailsService}.
     *
     * @param userDetailsService the custom user details service
     * @param passwordEncoder    the password encoder to use
     * @return an {@link AuthenticationManager} instance
     */
    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
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

                // Define public endpoints and require authentication for all others
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/logout", "/css/**", "/js/**", "/favicon.ico", "/api/registration/**").permitAll()
                        .anyRequest().authenticated()
                )

                // Use default Spring Security form login
                .formLogin(withDefaults())

                // Configure logout behavior
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") // Can be replaced with JSON for API use
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )

                /*
                 * This block can be extended to define a custom authentication entry point
                 * for REST APIs once the React frontend is integrated:
                 *
                 * .exceptionHandling(ex -> ex
                 *     .accessDeniedHandler(accessDeniedHandler)
                 *     .authenticationEntryPoint((request, response, authException) -> {
                 *         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                 *         response.getWriter().write("Unauthorized");
                 *     })
                 * )
                 */

                // Use custom access denied handler for forbidden requests
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler)
                );

        return http.build();
    }
}
