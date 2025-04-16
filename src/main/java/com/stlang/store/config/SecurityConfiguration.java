package com.stlang.store.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${api.path}")
    private String apiPrefix;

    @Autowired
    private JWTFilter jwtFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private CustomAuthenticatedEntryPoint customAuthenticatedEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder (new BCryptPasswordEncoder());
        return provider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.cors(Customizer.withDefaults());
        http.authorizeHttpRequests( request -> request
                        .requestMatchers(
                                String.format("%s/auth/login", apiPrefix),
                                String.format("%s/auth/register", apiPrefix),
                                String.format("%s/auth/account", apiPrefix),
                                String.format("%s/auth/refreshToken", apiPrefix),
                                String.format("%s/storages/**", apiPrefix),
                                String.format("%s/upload/files/**", apiPrefix),
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // CATEGORY //
                        .requestMatchers(HttpMethod.GET, String.format("%s/categories/**", apiPrefix)).permitAll()
                        .requestMatchers(HttpMethod.POST, String.format("%s/categories/**", apiPrefix)).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, String.format("%s/categories/**", apiPrefix)).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, String.format("%s/categories/**", apiPrefix)).hasRole("ADMIN")

                        // Product //
                        .requestMatchers(HttpMethod.GET, String.format("%s/products/**", apiPrefix)).permitAll()
                        .requestMatchers(HttpMethod.POST, String.format("%s/products/**", apiPrefix)).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, String.format("%s/products/**", apiPrefix)).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, String.format("%s/products/**", apiPrefix)).hasRole("ADMIN")

                        // role //
                        .requestMatchers(String.format("%s/roles/**", apiPrefix)).hasRole("ADMIN")
                        // Authority //
                        .requestMatchers(String.format("%s/authorities/**", apiPrefix)).hasRole("ADMIN")

                        // Account //
                        .requestMatchers(String.format("%s/account/**", apiPrefix)).hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(exceptions  ->
                exceptions.authenticationEntryPoint(customAuthenticatedEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
