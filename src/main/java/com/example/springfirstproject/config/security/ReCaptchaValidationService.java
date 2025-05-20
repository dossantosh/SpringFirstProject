package com.example.springfirstproject.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReCaptchaValidationService {
    @Value("${recaptcha.secret-key}")
    private String secretKey;

    @Value("${recaptcha.verify-url}")
    private String verifyUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean validateCaptcha(String token) {
        try {
            String url = verifyUrl + "?secret=" + secretKey + "&response=" + token;
            Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);

            if (response == null || !Boolean.TRUE.equals(response.get("success"))) {
                return false;
            }

            Double score = (Double) response.getOrDefault("score", 0.0);
            return score >= 0.5;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
