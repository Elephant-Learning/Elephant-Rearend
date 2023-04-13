package me.elephantsuite.stats.quiz_card;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@ToString
@Entity
public class QuizCardStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_card_statistics_generator")
    @SequenceGenerator(name = "quiz_card_statistics_generator", sequenceName = "quiz_card_statistics_sequence", allocationSize = 1)
    private Long id;

    private Long quizCardId;

    private boolean answeredCorrectly = false;

    public QuizCardStatistics(long quizCardId) {
        this.quizCardId = quizCardId;
    }
}
