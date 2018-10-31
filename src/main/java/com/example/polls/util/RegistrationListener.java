package com.example.polls.util;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.polls.model.User;
import com.example.polls.service.IUserService;

/**
 * @author <egadEldin.ext@orange.com> Essam Eldin
 *
 */
@Component
public class RegistrationListener implements
  ApplicationListener<OnRegistrationCompleteEvent> {
  
    @Autowired
    private IUserService service;
  
    @Autowired
    private MessageSource messages;
  
    @Autowired
    private JavaMailSender mailSender;
    
    Logger log=LoggerFactory.getLogger(this.getClass());
 
    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }
 
    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);
        log.info("URL============="+event.getAppUrl());
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl 
          = "http://localhost:3000/login?token=" + token;
        String message = "please activate your account by clicking the following link : ";
         
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message +" "+ confirmationUrl);
        mailSender.send(email);
        
    }
}
