package me.elephantsuite.quiz.controller;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class QuizRequest {

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static class CreateQuiz {
        private String name;

        private String description;

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
}
