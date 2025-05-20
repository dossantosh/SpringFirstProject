package com.example.springfirstproject.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.springfirstproject.models.Token;
import com.example.springfirstproject.models.User.User;
import com.example.springfirstproject.repositories.TokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TokenService {

  private final TokenRepository tokenRepository;
  private final JavaMailSender mailSender;

  public void sendVerificationEmail(String to, String token) {
    String subject = "Por favor, verifica tu cuenta para poder activarla";
    // String confirmationUrl = "https://tudominio.com/confirm?token=" + token;
    String confirmationUrl = "http://localhost:8083/confirm?token=" + token;
    String message = "Haz clic en el siguiente enlace para activar tu cuenta: " + confirmationUrl;

    SimpleMailMessage email = new SimpleMailMessage();
    email.setTo(to);
    email.setSubject(subject);
    email.setText(message);
    mailSender.send(email);
  }

  public String createVerificationToken(User user) {
    String token = UUID.randomUUID().toString();
    Token vToken = new Token();
    vToken.setToken(token);
    vToken.setUser(user);
    vToken.setExpiryDate(LocalDateTime.now().plusHours(24));
    tokenRepository.save(vToken);
    return token;
  }

  public Optional<Token> findByToken(String token) {
    return tokenRepository.findByToken(token);
  }

  public void deleteByToken(String token) {
    tokenRepository.deleteByToken(token);
  }
}
