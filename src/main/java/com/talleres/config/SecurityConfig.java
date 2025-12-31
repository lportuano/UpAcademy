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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/", "/index", "/login", "/academy/formulario", "/academy/formDocente", "/usuarios/registrarUsuario").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        .requestMatchers("/usuarios/gestionar/**").hasRole("ADMIN")
                        .requestMatchers("/academy/editarDocente/**","/academy/editarEstudiante/**").hasRole("ADMIN")
                        .requestMatchers("/academy/eliminarEstudiante/**").hasRole("ADMIN")

                        .requestMatchers("/horarios/**").hasAnyRole("ADMIN", "DOCENTE")
                        .requestMatchers("/cursos/**").hasAnyRole("ADMIN", "ESTUDIANTE", "DOCENTE")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("cedula")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/postLogin", true)
                        .permitAll()
                )


                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/index?error=forbidden");
                        })
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                );

        return http.build();
    }
}