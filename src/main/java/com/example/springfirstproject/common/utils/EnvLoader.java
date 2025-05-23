package com.example.springfirstproject.common.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
    public static void loadEnv() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
        System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
        System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));
        System.setProperty("EMAIL_HOST", dotenv.get("EMAIL_HOST"));
        System.setProperty("EMAIL_PORT", dotenv.get("EMAIL_PORT"));
        System.setProperty("EMAIL_USERNAME", dotenv.get("EMAIL_USERNAME"));
        System.setProperty("EMAIL_PASSWORD", dotenv.get("EMAIL_PASSWORD"));
        System.setProperty("MANAGEMENT_PORT", dotenv.get("MANAGEMENT_PORT"));
        System.setProperty("RECAPTCHA_SITE_KEY", dotenv.get("RECAPTCHA_SITE_KEY"));
        System.setProperty("RECAPTCHA_SECRET_KEY", dotenv.get("RECAPTCHA_SECRET_KEY"));
        System.setProperty("RECAPTCHA_VERIFY_URL", dotenv.get("RECAPTCHA_VERIFY_URL"));
        System.setProperty("RECAPTCHA_SCORE", dotenv.get("RECAPTCHA_SCORE"));
        System.setProperty("RECAPTCHA_ENABLED", dotenv.get("RECAPTCHA_ENABLED"));
    }
}
