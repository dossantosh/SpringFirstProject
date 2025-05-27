package com.dossantosh.springfirstproject.user.service.objects;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.models.objects.Token;
import com.dossantosh.springfirstproject.user.repository.objects.TokenRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenService {

  private final TokenRepository tokenRepository;
  private final JavaMailSender mailSender;

  public Optional<Token> findByToken(String token) {
    return tokenRepository.findByToken(token);
  }

  public void deleteByToken(String token) {
    tokenRepository.deleteByToken(token);
  }

  public boolean existsByUser(User user) {
    return tokenRepository.existsByUser(user);
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

  public void sendVerificationEmailUser(String to, String token) {
    String subject = "Seb's Fragances: Activar Cuenta";
    String confirmationUrl = "http://localhost:8083/confirm?token=" + token;
    String message = "Por favor, verifica tu cuenta para poder activarla. Haz clic en el siguiente: " + confirmationUrl;

    SimpleMailMessage email = new SimpleMailMessage();
    email.setTo(to);
    email.setSubject(subject);
    email.setText(message);
    mailSender.send(email);
  }

  public void sendVerificationEmailPassword(String to, String token) {
    String subject = "Seb's Fragances: Cambiar Contraseña";
    String confirmationUrl = "http://localhost:8083/forgotPassword?token=" + token;
    String message = "Por favor, verifica que eres tú para recuperar la contraseña: "
        + confirmationUrl + "\n  Si no eres tú, ignora este correo y revisa la seguridad de tu cuenta.";

    SimpleMailMessage email = new SimpleMailMessage();
    email.setTo(to);
    email.setSubject(subject);
    email.setText(message);
    mailSender.send(email);
  }
}
