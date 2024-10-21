package com.backend.ecommercebackend.service.impl;

import com.backend.ecommercebackend.cache.service.RedisActivationTokenService;
import com.backend.ecommercebackend.dto.request.EmailActivationRequest;
import com.backend.ecommercebackend.dto.request.EmailRequest;
import com.backend.ecommercebackend.enums.Exceptions;
import com.backend.ecommercebackend.exception.ApplicationException;
import com.backend.ecommercebackend.model.user.User;
import com.backend.ecommercebackend.model.user.UserEmail;
import com.backend.ecommercebackend.repository.user.EmailRepository;
import com.backend.ecommercebackend.repository.user.UserRepository;
import com.backend.ecommercebackend.service.EmailService;
import com.backend.ecommercebackend.cache.service.RedisVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  @Value("${spring.mail.username}")
  private String from;
  private final JavaMailSender mailSender;
  private final RedisVerificationService redisVerificationService;
  private final EmailRepository emailRepository;
  private final UserRepository userRepository;
  private final RedisActivationTokenService redisActivationTokenService;

  @Override
  public void sendVerificationCode(String email) {
    User userEmail = userRepository.findByEmail(email)
            .orElseThrow(() -> new ApplicationException(Exceptions.USER_NOT_FOUND));
    String verificationCode = createVerificationCode();
    String subject = "Şifrə dəyişikliyini təsdiqləmə kodunuz";
    String text = "təsdiqləmə kodu: " + verificationCode;
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(from);
    message.setSubject(subject);
    message.setText(text);
    message.setTo(userEmail.getEmail());
    mailSender.send(message);
    redisVerificationService.storeEmailVerificationCode(email, verificationCode);
  }

  @Override
  public void sendActivationLink(String email) {
    var userEmail = emailRepository.findByEmail(email);
    if (userEmail.isEmpty()) {
      String token = generateActivationToken();
      String activationUrl = generateActivationLink(token);
      SimpleMailMessage message = new SimpleMailMessage();
      String myEmail = from;
      message.setFrom(myEmail);
      message.setSubject("Hesabınızı aktivləşdirin");
      message.setText("Hesabınızı aktivləşdirmək üçün linkə klikləyin: " + activationUrl);
      message.setTo(email);
      mailSender.send(message);
      redisActivationTokenService.storeActivationToken(email, token);
    } else throw new ApplicationException(Exceptions.USER_ALREADY_EXIST);
  }

  @Override
  public void registerEmail(EmailRequest request) {
    var existingEmail = emailRepository.findByEmail(request.getEmail());
    if (existingEmail.isPresent()) {
      UserEmail userEmail = existingEmail.get();
      if (!userEmail.isVerified()) {
        if (userEmail.getCreatedAt().isBefore(LocalDateTime.now().minusHours(24))) {
          emailRepository.deleteByEmail(userEmail.getEmail());
        } else {
          sendActivationLink(userEmail.getEmail());
          return;
        }
      } else {
        throw new ApplicationException(Exceptions.USER_ALREADY_EXIST);
      }
    }
    UserEmail userEmail = UserEmail.builder()
            .email(request.getEmail())
            .verified(false)
            .createdAt(LocalDateTime.now())
            .build();
    sendActivationLink(userEmail.getEmail());
    emailRepository.save(userEmail);
  }

  @Override
  public void activateEmail(EmailActivationRequest request) {
    String storedToken = redisActivationTokenService.getActivationToken(request.getEmail());

    if (storedToken == null || !storedToken.equals(request.getToken())) {
      throw new ApplicationException(Exceptions.INVALID_TOKEN_EXCEPTION);
    }

    UserEmail userEmail = emailRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ApplicationException(Exceptions.USER_NOT_FOUND));

    userEmail.setVerified(true);

    emailRepository.save(userEmail);
    redisActivationTokenService.deleteActivationToken(request.getEmail());
  }

  @Override
  public void deleteStoredEmail(String email) {
    emailRepository.deleteByEmail(email);
  }

  private String createVerificationCode() {
    Random random = new Random();
    int verificationCode = 1000 + random.nextInt(9000);
    return String.valueOf(verificationCode);
  }

  private String generateActivationToken() {
    return UUID.randomUUID().toString();
  }

  private String generateActivationLink(String activationToken) {
    return "http://localhost:5173/activate?token=" + activationToken;
  }

  @Scheduled(fixedRate = 86400000)
  public void removeUnverifiedEmailsAfterTokenExpiration() {
    LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);
    List<UserEmail> unverifiedEmails = emailRepository.findAllByVerifiedFalseAndCreatedAtBefore(cutoffTime);
    for (UserEmail email : unverifiedEmails) {
      emailRepository.deleteByEmail(email.getEmail());
    }
  }
}
