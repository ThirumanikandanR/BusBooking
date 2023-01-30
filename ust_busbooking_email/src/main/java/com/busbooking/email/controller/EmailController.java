package com.busbooking.email.controller;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busbooking.email.response.EmailResponse;
import com.busbooking.email.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {
	
	@Autowired
	EmailService emailService;

	
	@PostMapping("/send")
	public String sendEmailWithAttachment(@RequestBody EmailResponse emailResponse) throws MessagingException, IOException {
	
		emailService.sendEmailWithAttachment(emailResponse);
		
		return "email success";
			
	}
}
