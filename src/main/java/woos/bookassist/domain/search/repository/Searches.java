package woos.bookassist.domain.search.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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
    private LocalDate searchDateTime;
}
