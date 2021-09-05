package rs.fon.njt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private int reservationId;

    @Column(name = "teacher_username")
    private String teacherUsername;
    @Column(name = "classroom_id")
    private int classroomId;


    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @Column(name = "capacity")
    private int capacity;
    @JsonIgnore
    @Column(name = "approved")
    private Boolean approved;

    @Transient
    private String fullName;
    @Transient
    private String classroomName;
}
