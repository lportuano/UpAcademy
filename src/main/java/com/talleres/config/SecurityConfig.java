package com.talleres.config;

import com.talleres.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioService usuarioService;

    // Inyectamos el servicio mediante constructor
    public SecurityConfig(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Definimos el Bean del encriptador aquí mismo
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(usuarioService);
        // Usamos el bean de arriba
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Permitir recursos estáticos sin login
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/vendor/**").permitAll()

                        // Restricción de acceso por Roles
                        .requestMatchers("/usuarios/gestionar/**").hasRole("ADMIN")
                        .requestMatchers("/academy/editarDocente/**").hasAnyRole("ADMIN", "DOCENTE")

                        // Todo lo demás requiere login
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/postLogin", true) // Lógica de redirección por rol
                        .usernameParameter("cedula")         // Usamos tu campo de cédula
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                );

        return http.build();
    }
}