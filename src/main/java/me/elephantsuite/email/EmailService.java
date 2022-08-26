package me.elephantsuite.email;

import java.util.Objects;
import java.util.function.Function;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public final class EmailService implements EmailSender {

	private final static Logger LOGGER = LogManager.getLogger(EmailService.class);
	private final JavaMailSender mailSender;

	@Override
	@Async
	public void send(String to, String email, boolean html) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

			helper.setText(email, html);
			helper.setTo(to);
			helper.setSubject("Verify your email");
			helper.setFrom(ElephantBackendApplication.ELEPHANT_CONFIG.getConfigOption("senderEmailAddress"));
			mailSender.send(mimeMessage);
		} catch (RuntimeException | MessagingException e) {
			LOGGER.error("Failed to send email", e);
			// for testing comment out
			throw new IllegalStateException("failed to send email");
		}
	}

}
