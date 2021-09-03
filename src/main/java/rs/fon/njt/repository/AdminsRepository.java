package rs.fon.njt.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rs.fon.njt.models.Admin;

@Repository
public interface AdminsRepository extends CrudRepository<Admin, String> {
}
