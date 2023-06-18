package me.elephantsuite.timeline.event;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.timeline.Timeline;
import me.elephantsuite.user.ElephantUser;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@Entity
@ToString
public class Event {


    @Id
    @SequenceGenerator(name = "event_sequence", sequenceName = "event_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_sequence")
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JsonBackReference
    private Timeline timeline;

    private LocalDateTime localDate;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Importance importance;

    public Event(Timeline timeline, LocalDateTime date, String name, String description, Importance importance) {
        this.timeline = timeline;
        this.localDate = date;
        this.name = name;
        this.description = description;
        this.importance = importance;
    }
}