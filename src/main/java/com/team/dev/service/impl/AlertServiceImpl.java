package com.team.dev.service.impl;

import com.team.dev.service.AlertService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class AlertServiceImpl implements AlertService {


    private final JavaMailSender mailSender;

    public AlertServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendLowMoistureAlert() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("user@example.com"); // Remplace par l'email du destinataire
        message.setSubject("Alerte humidité critique !");
        message.setText("L'humidité du sol est trop basse, veuillez vérifier le système d'irrigation.");

        mailSender.send(message);
        System.out.println("Alerte envoyée : Humidité du sol critique !");

    }
}
