package com.example.springfirstproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.example.springfirstproject.config.security.CaptchaValidationFilter;
import com.example.springfirstproject.config.security.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

        private final UserDetailsServiceImpl userDetailsService;
        private final CaptchaValidationFilter captchaValidationFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .sessionManagement(session -> session
                                                .sessionFixation(sessionFixation -> sessionFixation
                                                                .migrateSession() // Invalida la sesión anterior y crea
                                                                                  // una nueva
                                                ))
                                .addFilterBefore(captchaValidationFilter, UsernamePasswordAuthenticationFilter.class)
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/login", "/confirm", "/token-invalid",
                                                                "/register", "/css/**", "/js/**", "/images/**")
                                                .permitAll()
                                                .requestMatchers("/principal", "/usuarios", "/perfil/**",
                                                                "/admin/**",
                                                                "/configuracion/**", "/perfumes/**")
                                                .authenticated()
                                                .requestMatchers("/actuator/**").hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/principal", true)
                                                .failureUrl("/login?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())
                                .userDetailsService(userDetailsService);
                return http.build();
        }

        @Bean
        public CookieSerializer cookieSerializer() {
                DefaultCookieSerializer serializer = new DefaultCookieSerializer();
                serializer.setCookieName("JSESSIONID");
                serializer.setUseSecureCookie(true);
                serializer.setUseHttpOnlyCookie(true);
                serializer.setCookiePath("/");
                return serializer;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

}
