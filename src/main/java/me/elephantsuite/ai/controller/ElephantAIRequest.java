package me.elephantsuite.ai.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.elephantsuite.deck.DeckVisibility;

public class ElephantAIRequest {

	@Getter
	@EqualsAndHashCode
	@ToString
	public static class SendMessage {
		private final String prompt;

		@JsonCreator
		public SendMessage(String prompt) {
			this.prompt = prompt;
		}
	}

	@Getter
	@EqualsAndHashCode
	@ToString
	public static class CreateDeck {
		private final long userId;

		private final String topic;

		private final int termNumber;

		private final DeckVisibility deckVisibility;

		@JsonCreator
		public CreateDeck(long userId, int termNumber, String topic, DeckVisibility visibility) {
			this.userId = userId;
			this.topic = topic;
			this.termNumber = termNumber;
			this.deckVisibility = visibility;
		}
	}
}
