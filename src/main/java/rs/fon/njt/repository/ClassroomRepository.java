package rs.fon.njt.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rs.fon.njt.models.Classroom;

@Repository
public interface ClassroomRepository extends CrudRepository<Classroom, Integer> {
}
