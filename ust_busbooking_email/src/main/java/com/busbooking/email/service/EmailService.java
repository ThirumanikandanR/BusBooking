package com.busbooking.email.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.busbooking.email.response.EmailResponse;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;

	public void sendEmail(EmailResponse emailResponse) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(emailResponse.getToemail());
		message.setSubject(emailResponse.getSubject());
		message.setText(emailResponse.getBody());

		mailSender.send(message);
	}

	public void sendEmailWithAttachment(EmailResponse emailResponse)
			throws MessagingException, IOException {

		MimeMessage mimeMessage = mailSender.createMimeMessage();

		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

		mimeMessageHelper.setTo(emailResponse.getToemail());
		mimeMessageHelper.setSubject(emailResponse.getSubject());
		mimeMessageHelper.setText(emailResponse.getBody());

//		FileSystemResource fileSystem = new FileSystemResource(attachment);

		

		DataSource source = new ByteArrayDataSource( new ByteArrayInputStream(emailResponse.getInvoice()), "application/pdf");


		mimeMessageHelper.addAttachment("Ticket Details.pdf", source);

		mailSender.send(mimeMessage);

		System.out.println("Mail with attachment sent succssfully");
	}


}
