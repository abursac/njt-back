package rs.fon.njt.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rs.fon.njt.models.Reservation;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Reservation.ReservationId> {
}
