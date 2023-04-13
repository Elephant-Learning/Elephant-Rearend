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
	ANSWER("Elephant Answer"),
	COMMENT("Comment"),
	REPLY("Reply"),
	QUIZ("Quiz"),
	QUIZ_CARD("Quiz Card");

	final String name;

	InvalidIdType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
