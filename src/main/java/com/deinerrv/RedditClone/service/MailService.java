package com.deinerrv.RedditClone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.deinerrv.RedditClone.entity.NotificationEmail;
import com.deinerrv.RedditClone.exception.SpringRedditException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailService {
    
    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.validationAccount.sender}")
    private String senderEmail;

    @Async
    public void sendMail(NotificationEmail email){
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(senderEmail);
            messageHelper.setTo(email.getRecipient());
            messageHelper.setSubject(email.getSubject());
            messageHelper.setText(email.getBody());
        };

        try {
            mailSender.send(messagePreparator);
            log.info("Activation email sent!!");
        } 
        catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new SpringRedditException("Exception occurred when sending mail to " + email.getRecipient(), e);
        }
    }
}
