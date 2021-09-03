package rs.fon.njt.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.fon.njt.models.Teacher;

import java.util.Optional;

@Repository
public interface TeachersRepository extends CrudRepository<Teacher, String> {

    @Query("SELECT t FROM Teacher t WHERE t.username = :username")
    Optional<Teacher> getTeacherByUsername(@Param("username") String username);
}
