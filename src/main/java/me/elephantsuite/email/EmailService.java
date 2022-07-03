package me.elephantsuite.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

	private final static Logger LOGGER = LogManager.getLogger(EmailService.class);

	private final JavaMailSender mailSender;

	@Override
	@Async
	public void send(String to, String email) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

			helper.setText(email, true);
			helper.setTo(to);
			helper.setSubject("Verify your email");
			helper.setFrom("no-reply@elephantsuite.me");
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			LOGGER.error("Failed to send email", e);
			throw new IllegalStateException("failed to send email");
		}
	}
}
