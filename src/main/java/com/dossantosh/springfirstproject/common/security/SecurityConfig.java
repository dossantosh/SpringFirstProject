package com.dossantosh.springfirstproject.common.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer.SessionFixationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;
import com.dossantosh.springfirstproject.common.security.custom.UserDetailsServiceImpl;
import com.dossantosh.springfirstproject.common.security.custom.captcha.CaptchaValidationFilter;
import com.dossantosh.springfirstproject.common.security.custom.login.CustomAuthenticationFailureHandler;
import com.dossantosh.springfirstproject.common.security.custom.login.CustomAuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

        private final UserDetailsServiceImpl userDetailsService;
        private final CaptchaValidationFilter captchaValidationFilter;
        private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
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
                                                .requestMatchers("/common/**", "/objects/**", "/user/**")
                                                .authenticated()
                                                .requestMatchers("/actuator/**").hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .successHandler(customAuthenticationSuccessHandler)
                                                .failureHandler(customAuthenticationFailureHandler))
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID"))
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
