package me.elephantsuite.timeline.marker;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.timeline.Timeline;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@Entity
@ToString
public class Marker {

    @Id
    @SequenceGenerator(name = "marker_sequence", sequenceName = "marker_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "marker_sequence")
    private Long id;

    private String name;

    private String date;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JsonBackReference
    private Timeline timeline;

    public Marker(Timeline timeline, String name, String date) {
        this.timeline = timeline;
        this.name = name;
        this.date = date;
    }
}
