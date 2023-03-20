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
    private static final String [] PUBLIC_ENDPOINTS = {
            "/user/new/customer",
            "/user/login"
    };
    private static final String [] PRIVATE_ENDPOINTS_CUSTOMER = {
            "/test/customer"
    };
    private static final String [] PRIVATE_ENDPOINTS_ADMINISTRATOR = {
            "/user/new/employee",
            "/user/delete/{id}"
    };
    private static final String [] PRIVATE_ENDPOINTS_EMPLOYEE = {
            "/test/employee"
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(PRIVATE_ENDPOINTS_CUSTOMER).hasRole("CUSTOMER")
                .requestMatchers(PRIVATE_ENDPOINTS_ADMINISTRATOR).hasRole("ADMINISTRATOR")
                .requestMatchers(PRIVATE_ENDPOINTS_EMPLOYEE).hasRole("EMPLOYEE")
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
