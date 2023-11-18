package br.com.rodrigo.api.service.impl;

public interface EmailService {
    void sendEmail(String recipientEmail, String subject, String content);
}
