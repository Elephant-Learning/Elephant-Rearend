package me.elephantsuite.timeline;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.timeline.event.Event;
import me.elephantsuite.timeline.marker.Marker;
import me.elephantsuite.user.ElephantUser;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@Entity
@ToString
public class Timeline {

    @Id
    @SequenceGenerator(name = "timeline_sequence", sequenceName = "timeline_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timeline_sequence")
    private Long id;


    private String name;

    private TimelineVisibility timelineVisibility;

    private int likes = 0;

    private String description;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JsonBackReference
    @JoinColumn(name = "elephant_user_id", foreignKey = @ForeignKey(name = "elephant_user_id"))
    private ElephantUser user;

    @OneToMany(mappedBy = "timeline", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "timeline", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<Marker> markers = new ArrayList<>();

    public Timeline(ElephantUser user, String name, TimelineVisibility visibility, String description) {
        this.user = user;
        this.name = name;
        this.timelineVisibility = visibility;
        this.description = description;
    }


}
