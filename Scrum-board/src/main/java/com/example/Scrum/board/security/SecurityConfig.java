package com.example.Scrum.board.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                                .requestMatchers("/gager/**").hasAnyRole("MANAGER", "GAGER", "ADMIN")
                                .requestMatchers("/register").permitAll()// Разрешить доступ к странице регистрации без аутентификации
                                .requestMatchers("/index").hasAnyRole("MANAGER", "GAGER", "ADMIN")
                                .requestMatchers("/main").hasAnyRole("MANAGER", "GAGER", "ADMIN")
                                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll() // Разрешить доступ к статическим ресурсам
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // Указание пользовательской страницы входа
                                .defaultSuccessUrl("/index") // Перенаправление на страницу после успешной аутентификации
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/") // Указание страницы после выхода
                                .permitAll()
                );


        return http.build();
    }

    @Bean
    UserDetailsManager inMemoryUserDetailsManager() {
        var user1 = User.withUsername("user").password("{noop}password").roles("USER").build();
        var user2 = User.withUsername("2").password("{noop}2@mail").roles("USER", "ADMIN").build();
        return new InMemoryUserDetailsManager(user1, user2);
    }
}