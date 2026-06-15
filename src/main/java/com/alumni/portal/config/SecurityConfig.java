package com.alumni.portal.config;

import com.alumni.portal.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/dashboard", "/login",
                                "/register", "/create-admin",
                                "/css/**", "/js/**",
                                "/webjars/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/alumni/add", "/alumni/edit/**",
                                "/alumni/delete/**").hasAnyRole("ADMIN","ALUMNI")
                        .requestMatchers("/students/add", "/students/edit/**",
                                "/students/delete/**").hasRole("ADMIN")
                        .requestMatchers("/events/add", "/events/edit/**",
                                "/events/delete/**").hasRole("ADMIN")
                        .requestMatchers("/jobs/add", "/jobs/edit/**").hasAnyRole("ADMIN","ALUMNI")
                        .requestMatchers("/jobs/delete/**").hasRole("ADMIN")
                        .anyRequest().authenticated()

                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler())
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String role = authentication.getAuthorities()
                    .iterator().next().getAuthority();
            if (role.equals("ROLE_ADMIN")) {
                response.sendRedirect("/admin/dashboard");
            } else if (role.equals("ROLE_ALUMNI")) {
                response.sendRedirect("/alumni/dashboard");
            } else if (role.equals("ROLE_STUDENT")) {
                response.sendRedirect("/student/dashboard");
            } else {
                response.sendRedirect("/dashboard");
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return builder.build();
    }
}