package com.example.The_Ca_Nhan.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SercurityConfig {

    private final String[] PUBLIC_ENDPOINTS = {"/auth/login", "/auth/register", "/users"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }



//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity , TokenAuthenticationFilter tokenAuthenticationFilter) throws Exception {
//        return httpSecurity
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
//                        .anyRequest().authenticated()
//                        .anyRequest().permitAll()
//                )
//                .addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class)
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
//                        .jwtAuthenticationConverter(jwtAuthenticationConverter())))
//                .csrf(AbstractHttpConfigurer::disable)
//                .build();
//    }

    @Bean
    public SecurityFilterChain devFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }



    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new CustomAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }
}
