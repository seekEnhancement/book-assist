package woos.bookassist.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findFirstById(String id);

    @Query("select u from Users u where u.id = ?1 or u.email = ?2")
    List<Users> findByIdOrEmail(String id, String email);
}
