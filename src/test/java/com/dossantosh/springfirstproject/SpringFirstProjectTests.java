package com.dossantosh.springfirstproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootTest
class SpringFirstProjectTests {

    // BLOQUE ESTÁTICO: se ejecuta cuando se carga la clase, antes de SpringBootTest
    static {
        Dotenv dotenv = Dotenv.configure()
                              .ignoreIfMalformed()
                              .ignoreIfMissing()
                              .load();

        dotenv.entries().forEach(entry -> {
            String key = entry.getKey();
            String value = entry.getValue();
            if (System.getProperty(key) == null) {
                System.setProperty(key, value);
            }
        });
    }

    @Test
    void contextLoads() {
        // Ahora el ApplicationContext ya se creó con MANAGEMENT_PORT, EMAIL_HOST, etc.
    }
}