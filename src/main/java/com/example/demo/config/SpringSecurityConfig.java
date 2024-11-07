package com.example.demo.config;

import com.example.demo.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/registration", "/login", "/logout").permitAll() // Разрешаем доступ к этим страницам всем
                        .requestMatchers("/swagger-ui/**").hasRole("ADMIN") // Доступ к удалению только для ADMIN
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )
                .formLogin(form -> form // Настройка формы логина
                        //.loginPage("/login") // Указываем кастомную страницу логина
                        .permitAll() // Разрешаем доступ к странице логина всем
                        .defaultSuccessUrl("/notes/view/all", true) // Перенаправление после успешного входа
                        .failureUrl("/login?error=true") // Перенаправление при ошибке входа
                )
                .logout(logout -> logout // Настройка выхода из системы
                        .logoutUrl("/logout") // URL для выхода из системы
                        .logoutSuccessUrl("/login") // Перенаправление после выхода
                        .permitAll() // Разрешаем выход всем
                );

        return http.build();
    }


}