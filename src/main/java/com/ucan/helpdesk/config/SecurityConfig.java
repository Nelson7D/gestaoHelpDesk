package com.ucan.helpdesk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // Bean para codificar senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para gerenciar autenticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Configuração principal do Spring Security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Desativa CSRF (não recomendado em produção)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/tickets/**").authenticated() // Apenas usuários autenticados podem acessar endpoints de ticket
                        .requestMatchers("/usuarios/**").permitAll() // Permite acesso público à criação de usuários
                        .requestMatchers("/tecnicos/**").hasRole("TECNICO") // Apenas técnicos podem acessar endpoints relacionados
                        .requestMatchers("/supervisores/**").hasRole("SUPERVISOR") // Apenas supervisores podem acessar endpoints específicos
                        .requestMatchers("/administradores/**").hasRole("ADMINISTRADOR") // Apenas administradores podem acessar endpoints específicos
                        .anyRequest().permitAll() // Todas as outras requisições são permitidas
                )
                .httpBasic().and() // Habilita autenticação básica
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Não cria sessões
        return http.build();
    }
}