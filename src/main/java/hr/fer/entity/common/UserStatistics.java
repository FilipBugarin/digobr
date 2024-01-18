package hr.fer.entity.common;

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
@Table(name = "userStatistics", schema = "public")
public class UserStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private long userId;

    @Builder.Default
    private Integer totalSolved = 0;
    @Builder.Default
    private Integer totalCorrect = 0;
    @Builder.Default
    private Integer totalIncorrect = 0;
}
