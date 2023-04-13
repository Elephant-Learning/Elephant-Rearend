package me.elephantsuite.answers.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class ElephantAnswersRequest {

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class CreateAnswer {
		private final String title;

		private final String description;

		private final long userId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class ChangeTitle {
		private final String value;

		private final long answerId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class SetTags {
		private final List<Integer> tags;

		private final long answerId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class SetUserTags {
		private final List<Integer> tags;

		private final long userId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class LikeAnswer {
		private final long userId;

		private final long answerId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class AnswersForUser {
		private final long userId;

	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class EditAnswer {
		private final long answerId;

		private final String description;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class EditComment {
		private final long commentId;

		private final String description;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class CreateComment {
		private final String description;

		private final long userId;

		private final long answerId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class IncreaseScore {
		private final long userId;

		private final int score;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class CreateReply {
		private final String text;

		private final long userId;

		private final long commentId;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class EditReply {
		private final String text;

		private final long replyId;
	}
}
