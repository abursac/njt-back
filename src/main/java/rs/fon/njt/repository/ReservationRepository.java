package rs.fon.njt.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.fon.njt.models.Reservation;

import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Reservation.ReservationId> {

    @Query("SELECT r FROM Reservation r WHERE r.reservationId.classroomId = :id AND r.approved = TRUE")
    List<Reservation> findByClassroom_Id(@Param("id") Integer id);
}
