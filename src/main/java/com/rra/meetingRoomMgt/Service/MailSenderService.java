package com.rra.meetingRoomMgt.Service;

import com.rra.meetingRoomMgt.dto.MailBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class MailSenderService {

    @Autowired
    private final JavaMailSender javaMailSender;

    public MailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void EmailSender(MailBody mail) {
        SimpleMailMessage message = new SimpleMailMessage();

        // Log the email details
        System.out.println("Sending email to: " + mail.to());
        System.out.println("Subject: " + mail.subject());
        System.out.println("Text: " + mail.text());

        // Check for null fields
        if (mail.to() == null || mail.subject() == null || mail.text() == null) {
            throw new IllegalArgumentException("MailBody contains null fields.");
        }

        message.setTo(mail.to());
        message.setSubject(mail.subject());
        message.setText(mail.text());
        message.setSentDate(new Date());

        try {
            javaMailSender.send(message);
            System.out.println("Email sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

}
