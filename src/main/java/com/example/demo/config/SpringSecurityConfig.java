package com.example.demo.config;

import com.example.demo.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Конфигурация безопасности Spring для приложения.
 * <p>
 * Настраивает аутентификацию и авторизацию с использованием Spring Security.
 *
 * @author VladimirBoss
 */
@Configuration
@EnableWebSecurity
@EnableScheduling
@EnableMethodSecurity
public class SpringSecurityConfig {


    /**
     * Создает экземпляр PasswordEncoder для хеширования паролей.
     *
     * @return PasswordEncoder - реализация BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomUserDetailsService userDetailsService;


    /**
     * Конфигурирует цепочку фильтров безопасности.
     * <p>
     * Определяет правила доступа к URL-адресам и настраивает страницы входа и выхода.
     *
     * @param http - объект HttpSecurity для настройки безопасности.
     * @return SecurityFilterChain - настроенная цепочка фильтров безопасности.
     * @throws Exception - если возникает ошибка при настройке безопасности.
     * @see CustomUserDetailsService
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/registration", "/login", "/logout").permitAll()
                        .requestMatchers("/swagger-ui/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/notes/view/all", true)
                        .failureUrl("/login?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}