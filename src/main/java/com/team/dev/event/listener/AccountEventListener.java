package com.team.dev.event.listener;

import com.team.dev.event.AccountEvent;
import com.team.dev.model.User;
import com.team.dev.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountEventListener implements ApplicationListener<AccountEvent> {

    private final AuthService service;
    private  final JavaMailSender mailSender;

    private User theUser;

    @Override
    public void onApplicationEvent(AccountEvent event) {
        theUser = event.getUser();

        String verificationToken = UUID.randomUUID().toString();

        service.saveUserVerificationToken(theUser, verificationToken);

        String url = event.getApplicationUrl()+"/api/v1/auth/verifyByEmail?token="+verificationToken;
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Cliquez sur le lien pour finaliser votre inscription : {} ", url);
    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        //My adresse e-amil for valided a compte
        String email ="";
        if (theUser.getRoles().stream().toList().contains("ADMIN")) {
            String emailForSend = "bakorodembele6625@gmail.com";
            String subject = "Email Vérification";
            String senderName = "Dev teams";

            /*String mailContentOwer = "<p> Hi, " + theUser.getPrenom() +" "+ theUser.getNom() +", </p>" +
                    "<p>Merci de votre inscription chez nous, " + "" +
                    "Veuillez suivre le lien ci-dessous pour finaliser votre inscription.</p>" +
                    "<a href=\"" + url + "\">Vérifiez votre email pour activer votre compte</a>" +
                    "<p>Merci <br> Service de portail d'enregistrement des utilisateurs";*/
            String mailContent = "<p> Hi, " + theUser.getPrenom() +" "+ theUser.getNom() +", </p>" +
                    "<p>Merci de votre inscription chez nous, " + "" +
                    "<p>Merci <br> Service de portail d'enregistrement des utilisateurs";
            String mailContentOwer = "<p> Hi, une entreprise vient de  s'incrire sur votre plateforme." + theUser.getPrenom() +" "+ theUser.getNom() +", </p>" +
                    "<p> Voici l'entreprsie, " + theUser.getEntreprise().getNom() +" "+ theUser.getEntreprise().getEmail() +" et l'adresse"+ theUser.getEntreprise().getAdresse() +""+
                    "Vous pouvez cliquez sur le lien ci-dessous pour activer son compte ou via l'ppli.</p>" +
                    "<a href=\"" + url + "\">Vérifiez votre email pour activer le compte de cette entreprise</a>" +
                    "<p>Merci <br> Service de portail d'enregistrement des utilisateurs";
            MimeMessage message = mailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(message);
            messageHelper.setFrom("devopsteamlight@gmail.com", senderName);
            messageHelper.setTo(emailForSend);
            messageHelper.setSubject(subject);

            if (theUser.getEmail() != null){
                messageHelper.setTo(theUser.getEmail());
                messageHelper.setText(mailContent, true);
            }
            messageHelper.setText(mailContentOwer, true);
            mailSender.send(message);
        }
    }

    public void sendPasswordResetVerification() throws MessagingException, UnsupportedEncodingException {
        String emailForSend = "bakorodembele6625@gmail.com";
        String subject = "Email Vérification";
        String senderName = "Dev teams";
        String url = "";

            String mailContent = "<p> Hi, " + theUser.getPrenom() +" "+ theUser.getNom() +", </p>" +
                    "<p>Merci d'avoir utiliser notre service, " + "" +
                    "Veuillez suivre le lien ci-dessous pour verifier votre compte.</p>" +
                    "<a href=\"" + url + "\">Vérifiez votre email pour la récuperation de votre compte</a>" +
                    "<p>Merci <br> Service de portail d'enregistrement des utilisateurs";

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("devopsteamlight@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);

        messageHelper.setText(mailContent, true);

        mailSender.send(message);
    };

    public void senPasswordResetVerifyEmail(String url, User user) throws MessagingException, UnsupportedEncodingException {

            String emailForSend = "bakorodembele6625@gmail.com";
            String subject = "Vérification d'email";
            String senderName = "Dev teams";

            String mailContent = "<p> Hi, " + user.getPrenom() +" "+ user.getNom() +", </p>" +
                    "<p>Vous avez récemment démandé à changer votre mot de passe, " + "" +
                    "<p>Veuillez suivre le lien ci-dessous, " + "" +
                    "pour changer votre mot de passe.</p>" +
                    "<a href=\"" + url + "\">Vérifiez votre email pour récupére votre compte</a>" +
                    "<p>Merci <br> Service de portail d'enregistrement des utilisateurs";

            MimeMessage message = mailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(message);
            messageHelper.setFrom(emailForSend, senderName);
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject(subject);

            messageHelper.setText(mailContent, true);

            mailSender.send(message);
    }

}

