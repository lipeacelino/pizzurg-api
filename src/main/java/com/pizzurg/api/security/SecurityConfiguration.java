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

    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/users/login",
            "/users/customers"
    };

    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED_TO_GET_STATUS = {
            "/products", //get
            "/products/{productId}", //get
            "/products/category/{categoryName}", //get
            "/products/search", //get
            "/{productId}", //get
            "/orders", //get
            "/orders/{orderId}", //get
            "/orders/status/{statusName}" //get
    };

    private static final String [] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED_TO_POST_STATUS = {
            "/orders" //post
    };

    private static final String [] ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_POST_STATUS = {
            "/products", //post
            "/products/{productId}/variation" //post
    };

    private static final String [] ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_PUT_STATUS = {
            "/{productId}/variation/{productVariationId}" //put
    };

    private static final String [] ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_PATCH_STATUS = {
            "/products/{productId}", //patch
            "/orders/{orderId}/status" //patch
    };

    private static final String [] ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_DELETE_STATUS = {
            "/users/{userId}",//delete
            "/products/{productId}", //delete
            "/{productId}/variation/{productVariationId}" //delete
    };

//    private static final String [] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = {
//            //endpoints dos produtos
//            "/products", //get
//            "/products/{productId}", //get
//            "/products/category/{categoryName}", //get
//            "/products/search", //get
//            "/{productId}", //get
//
//            //endpoints das ordens
//            "/orders", //post //get
//            "/orders/{orderId}", //get
//            "/orders/status/{statusName}" //get
//    };

//    private static final String [] ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY = {
//            //endpoints de usuários
//            "/users/{userId}",//delete
//
//            //endpoints dos produtos
//            "/products", //post
//            "/products/{productId}", //delete
//            "/products/{productId}/variation", //post
//            "/{productId}/variation/{productVariationId}", //put //delete
//
//            //endpoints das ordens
//            "/orders/{orderId}/status" //patch
//    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeHttpRequests()
                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                .requestMatchers(HttpMethod.GET, ENDPOINTS_WITH_AUTHENTICATION_REQUIRED_TO_GET_STATUS).authenticated()
                .requestMatchers(HttpMethod.POST, ENDPOINTS_WITH_AUTHENTICATION_REQUIRED_TO_POST_STATUS).authenticated()
                .requestMatchers(HttpMethod.POST, ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_POST_STATUS).hasAnyRole(ROLE_ADMINISTRATOR)
                .requestMatchers(HttpMethod.PUT, ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_PUT_STATUS).hasAnyRole(ROLE_ADMINISTRATOR)
                .requestMatchers(HttpMethod.PATCH, ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_PATCH_STATUS).hasAnyRole(ROLE_ADMINISTRATOR)
                .requestMatchers(HttpMethod.DELETE, ENDPOINTS_AVAILABLE_FOR_ADMIN_ONLY_TO_DELETE_STATUS).hasAnyRole(ROLE_ADMINISTRATOR)
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