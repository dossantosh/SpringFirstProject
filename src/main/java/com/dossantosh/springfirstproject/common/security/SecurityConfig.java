package com.dossantosh.springfirstproject.common.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer.SessionFixationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;

import com.dossantosh.springfirstproject.common.security.custom.CustomUserDetailsService;
import com.dossantosh.springfirstproject.common.security.custom.captcha.CaptchaValidationFilter;
import com.dossantosh.springfirstproject.common.security.custom.login.CustomAuthenticationFailureHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        private final CustomUserDetailsService customUserDetailsService;
        private final CaptchaValidationFilter captchaValidationFilter;
        private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .sessionManagement(session -> session
                                                .sessionFixation(SessionFixationConfigurer::migrateSession))
                                .addFilterBefore(captchaValidationFilter, UsernamePasswordAuthenticationFilter.class)
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/login", "forgotPasswordEmail", "/forgotPassword",
                                                                "/confirm/**", "/token-invalid",
                                                                "/register", "/css/**", "/js/**", "/images/**")
                                                .permitAll()
                                                .requestMatchers("/objects/perfume/liberar").permitAll()
                                                .requestMatchers("/common/**", "/objects/**", "/user/**")
                                                .authenticated()
                                                .requestMatchers("/actuator/**").hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/objects/news")
                                                .failureHandler(customAuthenticationFailureHandler))
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID"))
                                .userDetailsService(customUserDetailsService)
                                .headers(headers -> headers
                                                .contentSecurityPolicy(csp -> csp
                                                                .policyDirectives(
                                                                                "script-src 'self' https://cdn.jsdelivr.net https://www.google.com https://www.gstatic.com 'unsafe-inline' 'unsafe-eval'; "
                                                                                                +
                                                                                                "style-src 'self' https://cdn.jsdelivr.net 'unsafe-inline'; "
                                                                                                +
                                                                                                "img-src 'self' data: https://www.google.com https://www.gstatic.com; "
                                                                                                +
                                                                                                "font-src 'self' https://cdn.jsdelivr.net; "
                                                                                                +
                                                                                                "connect-src 'self'  https://www.google.com https://www.gstatic.com; "
                                                                                                +
                                                                                                "frame-src https://www.google.com https://www.gstatic.com; "
                                                                                                +
                                                                                                "frame-ancestors 'none';"))
                                                .frameOptions(frame -> frame.deny()) // evitar iframes
                                                .httpStrictTransportSecurity(hsts -> hsts
                                                                .includeSubDomains(true)
                                                                .maxAgeInSeconds(31536000)) // 1 año de HSTS
                                                .referrerPolicy(referrer -> referrer.policy(
                                                                ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER)))
                                // .requiresChannel(channel -> channel.anyRequest().requiresSecure())
                                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")); // Si tienes APIs sin CSRF
                return http.build();
        }

        @Bean
        public CookieSerializer cookieSerializer() {
                DefaultCookieSerializer serializer = new DefaultCookieSerializer();
                serializer.setCookieName("JSESSIONID");
                serializer.setUseSecureCookie(true);
                serializer.setUseHttpOnlyCookie(true);
                serializer.setCookiePath("/");
                serializer.setSameSite("Lax");
                return serializer;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public HttpSessionIdResolver httpSessionIdResolver() {
                return new CookieHttpSessionIdResolver();
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
                return new JdbcTemplate(dataSource);
        }
}
