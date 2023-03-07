package me.elephantsuite.quiz;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import me.elephantsuite.quiz.card.QuizCard;
import me.elephantsuite.user.ElephantUser;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
public class Quiz {

    @Id
    @SequenceGenerator(name = "quiz", sequenceName = "quiz_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_sequence")
    private Long id;

    private String name;

    private String description;

    private Double timeLimit = -1.0;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<QuizCard> cards = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JsonBackReference
    private ElephantUser user;

    public Quiz(String name, String description, ElephantUser user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }

}
