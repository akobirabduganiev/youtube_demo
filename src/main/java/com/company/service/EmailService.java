package com.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void send(String toEmail, String title, String content) {
        SimpleMailMessage simple = new SimpleMailMessage();
        simple.setTo(toEmail);
        simple.setSubject(title);
        simple.setText(content);
        javaMailSender.send(simple);
    }
}
