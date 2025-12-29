package com.talleres.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                                .requestMatchers(
                                        "/academy/comprar",
                                        "/academy/confirmarSeleccion",
                                        "/academy/formulario",
                                        "/academy/enviarEstudent",
                                        "/academy/registro_exitoso"
                                ).permitAll()

                        .requestMatchers("/usuarios/**", "/admin/**").hasRole("ADMIN")
                        .requestMatchers("/academy/editarEstudiante/**", "/academy/eliminarEstudiante/**").hasRole("ADMIN")

                        .requestMatchers("/academy").hasAnyRole("ADMIN", "DOCENTE")

                        //.requestMatchers("/academy/**").authenticated()
                        //.anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/login/postLogin", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index")
                        .permitAll()
                );
        return http.build();
    }
}