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
    private static final String ROLE_CUSTOMER = "CUSTOMER";
    private static final String ROLE_ADMINISTRATOR = "ADMINISTRATOR";
    private static final String ROLE_EMPLOYEE = "EMPLOYEE";
    public static final String [] NO_AUTH_ENDPOINTS = {
            "/users/customers",
            "/users/login"
    };
    private static final String [] PUBLIC_ENDPOINTS_PRODUCT = {
            "/products",
            "/products/{id}",
            "/products/category/{categoryName}",
            "/products/search"
    };
    private static final String [] PRIVATE_ENDPOINTS_PRODUCT = {
            "/products",
            "/products/{id}",
            "/products/{productId}/sizes/{productSizeId}"
    };
    private static final String [] PRIVATE_ENDPOINTS_CUSTOMER = {
            "/test/customer"
    };
    private static final String [] PRIVATE_ENDPOINTS_ADMINISTRATOR = {
            "/users/employees",
            "/users/{id}"
    };
    private static final String [] PRIVATE_ENDPOINTS_EMPLOYEE = {
            "/test/employee"
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, NO_AUTH_ENDPOINTS).permitAll()
                .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS_PRODUCT).authenticated()
                .requestMatchers(PRIVATE_ENDPOINTS_PRODUCT).hasAnyRole(ROLE_ADMINISTRATOR, ROLE_EMPLOYEE)
                .requestMatchers(PRIVATE_ENDPOINTS_CUSTOMER).hasRole(ROLE_CUSTOMER)
                .requestMatchers(PRIVATE_ENDPOINTS_ADMINISTRATOR).hasRole(ROLE_ADMINISTRATOR)
                .requestMatchers(PRIVATE_ENDPOINTS_EMPLOYEE).hasRole(ROLE_EMPLOYEE)
                .anyRequest().authenticated()
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