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

    private String startDate;

    private String endDate;

    private String name;

    private String description;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String image;

    @Enumerated(EnumType.STRING)
    private Importance importance;

    public Event(Timeline timeline, String date, String name, String description, Importance importance, String endDate, String image) {
        this.timeline = timeline;
        this.startDate = date;
        this.name = name;
        this.image = image;
        this.description = description;
        this.importance = importance;
        this.endDate = endDate;
    }
}