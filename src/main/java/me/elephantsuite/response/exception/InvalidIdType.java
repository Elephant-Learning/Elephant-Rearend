package me.elephantsuite.response.exception;

public enum InvalidIdType {
	USER("User"),
	DECK("Deck"),
	CARD("Card"),
	RESET_TOKEN("Reset Password Token"),
	NOTIFICATION("Notification"),
	CONFIRMATION_TOKEN("Confirmation Token"),
	FOLDER("Folder"),
	TIMELINE("Timeline"),
	EVENT("Event"),
	ANSWER("Elephant Answer"),
	COMMENT("Comment"),
	REPLY("Reply"),
	QUIZ("Quiz"),
	QUIZ_CARD("Quiz Card"),
	MARKER("Marker");

	final String name;

	InvalidIdType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
