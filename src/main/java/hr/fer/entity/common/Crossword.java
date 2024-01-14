package hr.fer.entity.common;

import hr.fer.entity.auth.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "crossword", schema = "public")
public class Crossword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String crosswordDefinition;

    @ManyToOne
    @JoinColumn(name = "puzzle_difficulty_id")
    private PuzzleDifficulty difficulty;

    @ManyToOne
    @JoinColumn(name = "puzzle_topic_id")
    private PuzzleTopic topic;

    @OneToMany(mappedBy = "crossword", cascade = CascadeType.ALL)
    private Set<UserCrossword> userCrosswords = new HashSet<>();

    @Builder.Default
    private Integer likes = 0;
}
