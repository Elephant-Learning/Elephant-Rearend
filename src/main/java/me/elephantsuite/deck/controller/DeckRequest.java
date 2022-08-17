package me.elephantsuite.deck.controller;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.elephantsuite.deck.DeckVisibility;


public class DeckRequest {

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class CreateDeck {
		private final Map<String, List<String>> terms;

		private final long authorId;

		private final String name;

		private final DeckVisibility visibility;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class RenameDeck {

		private final String newName;

		private final long deckId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class ResetTerms {

		private final Map<String, List<String>> newTerms;

		private final long deckId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class LikeDeck {

		private final long userId;

		private final long deckId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class ChangeVisiblity {

		private final long deckId;

		private final DeckVisibility visibility;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class ShareDeck {

		private final long deckId;

		private final long sharedUserId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class GetByName {
		private final long userId;

		private final String name;
	}
}
