package hr.fer.entity.common;

import hr.fer.entity.auth.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "userCrossword", schema = "public")
public class UserCrossword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "crossword_id")
    private Crossword crossword;

    @Builder.Default
    private boolean liked = false;
    @Builder.Default
    private Integer correctAnswers = 0;
    @Builder.Default
    private Integer incorectAnswers = 0;
}
