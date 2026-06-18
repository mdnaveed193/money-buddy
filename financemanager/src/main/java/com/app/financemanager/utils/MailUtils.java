package com.app.financemanager.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailUtils {

	@Autowired
	private JavaMailSender mailSender;
	
	public void sendActivationMail(String email, String activationLink) {
		System.out.println("mailSender s called");
		SimpleMailMessage message= new SimpleMailMessage();
		message.setFrom("moneybuddy101@gmail.com");
		message.setTo(email);
		message.setSubject("Activate Your account");
		message.setText("Click the link below to activate your account.\n\n" + activationLink);
		
		System.out.println("Sending mail to: "+ email);
		mailSender.send(message);
		System.out.println(message);
		System.out.println("email has been sent, please check...");
	}
}
