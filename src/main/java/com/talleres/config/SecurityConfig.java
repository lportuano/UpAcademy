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
                        // 1. Permisos públicos: Cualquier persona puede ver el inicio y registrarse
                        .requestMatchers("/", "/index", "/login", "/academy/formulario", "/academy/formDocente", "/usuarios/registrarUsuario").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/vendor/**", "/academy/**").permitAll()

                        // 2. Estudiantes: Acceso a sus cursos (la lógica del nivel se maneja en el Controller)
                        .requestMatchers("/cursos/**").hasAnyRole("ADMIN", "ESTUDIANTE", "DOCENTE")

                        // 3. Docentes: Pueden gestionar horarios y editar cursos
                        .requestMatchers("/horarios/**").hasAnyRole("ADMIN", "DOCENTE")

                        // 4. Administrador: Acceso total a la gestión de usuarios
                        .requestMatchers("/usuarios/gestionar/**").hasRole("ADMIN")
                        .requestMatchers("/academy/editarDocente/**").hasRole("ADMIN")
                        .requestMatchers("/academy/eliminarEstudiante/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("cedula")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/postLogin", true)
                        .permitAll()
                )

                //mensaje de error
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