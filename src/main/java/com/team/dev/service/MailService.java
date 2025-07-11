package com.team.dev.service;

import com.team.dev.dto.MailBody;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleMail(MailBody mailBody) throws MessagingException, UnsupportedEncodingException {
        String senderName = "Dev teams";

        String mailContent = "<p> Bonjour, </p>" +
                "<p>Voici le code Otp pour le mot de passe oublié : "+mailBody.text()+ "</p>";

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("devopsteamlight@gmail.com", senderName);
        messageHelper.setTo(mailBody.to());
        messageHelper.setSubject(mailBody.subject());

        messageHelper.setText(mailContent, true);

        /*mailSender.send(message);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailBody.to());
        message.setFrom(senderMail);
        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());*/
        mailSender.send(message);
    }
}
