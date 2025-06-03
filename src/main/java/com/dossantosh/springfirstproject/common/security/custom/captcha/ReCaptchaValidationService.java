package com.dossantosh.springfirstproject.common.security.custom.captcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
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
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("secret", secretKey);
            body.add("response", token);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

            // ResponseEntity<Map> responseEntity = restTemplate.postForEntity(verifyUrl, requestEntity, Map.class);

            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    verifyUrl,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            Map<String, Object> response = responseEntity.getBody();
            if (response == null || !Boolean.TRUE.equals(response.get("success"))) {
                return false;
            }

            Double score = 0.0;
            if (response.containsKey("score")) {
                Object scoreObj = response.get("score");
                if (scoreObj instanceof Number) {
                    score = ((Number) scoreObj).doubleValue();
                }
            }
            return score >= 0.5;
        } catch (ResourceAccessException rae) {
            rae.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
