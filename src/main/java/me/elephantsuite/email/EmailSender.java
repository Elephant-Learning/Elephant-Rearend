package me.elephantsuite.email;

public interface EmailSender {
	void send(String to, String email, String subject, boolean html);
}
