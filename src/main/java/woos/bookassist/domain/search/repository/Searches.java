package woos.bookassist.domain.search.repository;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Searches")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Searches {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long searchNumber;
    private String query;
    private String userId;
    private LocalDateTime searchDateTime;
}
