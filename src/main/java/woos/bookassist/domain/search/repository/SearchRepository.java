package woos.bookassist.domain.search.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<Searches, Long> {
    List<Searches> findByUserIdOrderBySearchDateTimeDesc(String userId);
}
