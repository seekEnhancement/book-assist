package woos.bookassist.domain.search.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends JpaRepository<Searches, Long> {

    List<Searches> findByUserIdOrderBySearchDateTimeDesc(String userId);

    @Query(value = "SELECT "
            + "query, queryCount "
            + "FROM ("
            + "SELECT query, count(*) as queryCount "
            + "FROM Searches "
            + "GROUP BY query "
            + "ORDER BY queryCount DESC) AS A LIMIT 10", nativeQuery = true)
    List<QueryRecommend> findTop10Queries();
}
