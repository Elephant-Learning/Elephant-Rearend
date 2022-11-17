package me.elephantsuite.timeline;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.elephantsuite.user.ElephantUser;

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

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JsonBackReference
    @JoinColumn(name = "elephant_user_id", foreignKey = @ForeignKey(name = "elephant_user_id"))
    private ElephantUser user;

    private String name;

    private TimelineVisibility timelineVisibility;

    private int likes;

}
