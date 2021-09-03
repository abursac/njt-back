package rs.fon.njt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class ReservationId implements Serializable {
        @Column(name = "teacher_username")
        private String teacherUsername;
        @Column(name = "classroom_id")
        private int classroomId;
    }
    @EmbeddedId
    private ReservationId reservationId;
    @Column(name = "date_time")
    private LocalDateTime dateTime;
}
