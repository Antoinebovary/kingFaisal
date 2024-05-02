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

    public  void EmailSender(MailBody mail){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.to());
        message.setSubject(mail.subject());
        message.setText(mail.text());
        message.setSentDate(new Date());
        javaMailSender.send(message);
    }
}
