package com.dossantosh.springfirstproject;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class }, scanBasePackages = {
        "com.dossantosh.springfirstproject.common",
        "com.dossantosh.springfirstproject.news",
        "com.dossantosh.springfirstproject.perfume",
        "com.dossantosh.springfirstproject.preferences",
        "com.dossantosh.springfirstproject.user"
})
@EnableTransactionManagement
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableJdbcHttpSession
@EnableWebSecurity
public class SpringFirstProject {

    public static void main(String[] args) {
        // 1) Cargar .env usando dotenv-java
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMalformed() // ignora si el .env tiene líneas mal formateadas
                .ignoreIfMissing() // no falle si no encuentra .env
                .load();

        // 2) Poner cada par clave=valor de .env en System.properties
        dotenv.entries().forEach(entry -> {
            String key = entry.getKey();
            String value = entry.getValue();
            // Solo definir si no existe ya la propiedad en System
            if (System.getProperty(key) == null) {
                System.setProperty(key, value);
            }
        });

        SpringApplication.run(SpringFirstProject.class, args);
    }

}