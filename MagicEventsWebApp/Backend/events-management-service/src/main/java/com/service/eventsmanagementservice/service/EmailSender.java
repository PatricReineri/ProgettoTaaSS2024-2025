package com.service.eventsmanagementservice.service;

import com.service.eventsmanagementservice.model.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public Boolean sendMail(EmailDetails details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
            System.out.println("Email sent successfully to : " + details.getRecipient());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
