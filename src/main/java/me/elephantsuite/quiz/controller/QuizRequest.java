package me.elephantsuite.quiz.controller;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

public class QuizRequest {

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static class CreateQuiz {
        private String name;

        private String description;

        private final Map<String, List<String>> cards;

        private long userId;
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static class EditNameAndDescription {
        private String name;

        private String description;

        private long quizId;
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static class SetQuizCards {
        private final Map<String, List<String>> newTerms;

        private final long quizId;
    }
}
