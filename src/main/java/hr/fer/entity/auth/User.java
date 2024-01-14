package hr.fer.entity.auth;

import hr.fer.entity.common.Crossword;
import hr.fer.entity.common.UserCrossword;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "user", schema = "public")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String name;
    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserCrossword> userCrosswords = new HashSet<>();
    
}
