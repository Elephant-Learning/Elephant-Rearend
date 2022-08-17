package me.elephantsuite.folder.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class FolderRequest {

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class CreateFolder {
		private final String name;

		private final long userId;

		private final List<Long> deckIds;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class AddDeck {
		private final long folderId;

		private final long deckId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class RemoveDeck {
		private final long folderId;

		private final long deckId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class SetName {
		private final long folderId;

		private final String name;
	}
}
