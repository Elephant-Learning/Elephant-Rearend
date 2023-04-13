package me.elephantsuite.quiz.card;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import me.elephantsuite.quiz.QuestionType;
import me.elephantsuite.quiz.Quiz;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
//@ToString(exclude = "quiz")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
public class QuizCard {

    @Id
    @SequenceGenerator(name = "quiz_card_sequence", sequenceName = "quiz_card_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_card_sequence")
    private Long id;

    private String term;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "quiz_card_id")
    private List<String> definitions;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JsonBackReference
    private Quiz quiz;

    public QuizCard(String term, List<String> definitions, Quiz quiz, QuestionType type) {
        this.term = term;
        this.definitions = definitions;
        this.quiz = quiz;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }
}
