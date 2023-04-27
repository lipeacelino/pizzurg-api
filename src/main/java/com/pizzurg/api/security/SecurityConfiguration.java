package com.pizzurg.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private AuthorizationFilter authorizationFilter;
    private static final String ROLE_ADMINISTRATOR = "ADMINISTRATOR";
    private static final String ROLE_EMPLOYEE = "EMPLOYEE";
    public static final String [] NO_AUTH_ENDPOINTS = {
            "/users/customers",
            "/users/login"
    };
    private static final String [] ENDPOINTS_PRODUCT = {
            "/products",
            "/products/{productId}",
            "/products/category/{categoryName}",
            "/products/search",
            "/{productId}/variation/{variationId}",
            "/{productId}"
    };

    private static final String [] ENDPOINTS_ORDER = {
            "/orders",
            "/orders/{orderId}",
            "/orders/status/{statusName}",
            "/orders/{orderId}/status"
    };
    private static final String [] PRIVATE_ENDPOINTS_PRODUCT = {
            "/products",
            "/products/{productId}",
            "/products/{productId}/sizes/{productVariantId}"
    };
    private static final String [] PRIVATE_ENDPOINTS_ADMINISTRATOR = {
            "/users/employees",
            "/users/{productId}"
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, NO_AUTH_ENDPOINTS).permitAll()
                .requestMatchers(HttpMethod.GET, ENDPOINTS_PRODUCT).authenticated()
                .requestMatchers(PRIVATE_ENDPOINTS_PRODUCT).hasAnyRole(ROLE_ADMINISTRATOR, ROLE_EMPLOYEE)
                .requestMatchers(PRIVATE_ENDPOINTS_ADMINISTRATOR).hasRole(ROLE_ADMINISTRATOR)
                .requestMatchers(ENDPOINTS_ORDER).authenticated()
                .anyRequest().denyAll()
                .and().addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}