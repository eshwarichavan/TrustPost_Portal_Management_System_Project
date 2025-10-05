package com.example.TrustPostApplication.Config;

import com.example.TrustPostApplication.Filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/users/create").hasRole("SUPER_ADMIN") // only super admin can access this

                        .requestMatchers("/api/auth/**").permitAll() // public access to login/signup

                        //.requestMatchers("/api/posts/**").hasAnyRole("USER", "MODERATOR", "SUPER_ADMIN")  //access to do post operations
                        .requestMatchers("/api/posts/**").permitAll()
                        .requestMatchers("/api/comments/**").permitAll()
                                .requestMatchers(" https://bannered-odilia-unwastefully.ngrok-free.dev/**").permitAll()


                        //allows swagger and api-docs , if this block of code not written it will denied the access
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        //change to make..
                        // .anyRequest().permitAll()  //allow everything
                        .anyRequest().authenticated() // all other endpoints require auth
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();

    }


    //password encoder :
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //for authentication manager :
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }
}
