package rs.fon.njt.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rs.fon.njt.models.User;

@Repository
public interface UsersRepository extends CrudRepository<User, String> {
}
