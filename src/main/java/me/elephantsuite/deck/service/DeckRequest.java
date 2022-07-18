package me.elephantsuite.deck.service;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


public class DeckRequest {

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class CreateDeck {
		private final Map<String, List<String>> terms;

		private final long authorId;

		private final String name;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class RenameDeck {

		private final String newName;

		private final long id;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class AddTerms {

		private final Map<String, List<String>> newTerms;

		private final long id;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class LikeDeck {

		private final long userId;

		private final long deckId;
	}
}
