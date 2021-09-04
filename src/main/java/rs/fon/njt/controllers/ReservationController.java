package rs.fon.njt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.fon.njt.models.Classroom;
import rs.fon.njt.models.Reservation;
import rs.fon.njt.models.Teacher;
import rs.fon.njt.repository.ClassroomRepository;
import rs.fon.njt.repository.ReservationRepository;
import rs.fon.njt.repository.TeachersRepository;

import java.util.List;

@RestController
@RequestMapping("reservations")
public class ReservationController {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private TeachersRepository teachersRepository;
    @Autowired
    private ClassroomRepository classroomRepository;

    @GetMapping()
    public Iterable<Reservation> getReservations(){
        return reservationRepository.findAll();
    }

    @GetMapping("{id}")
    @ResponseBody
    public List<Reservation> getReservationsByClassroom(@PathVariable("id") Integer id)
    {
        List<Reservation> listRes =  reservationRepository.findByClassroom_Id(id);
        listRes.stream().forEach(
                reservation -> {
                    Teacher teacher = teachersRepository.getTeacherByUsername(reservation.getReservationId().getTeacherUsername()).get();
                    Classroom classroom = classroomRepository.findById(reservation.getReservationId().getClassroomId()).get();
                    reservation.setFullName(teacher.getFirstName() +" "+ teacher.getLastName());
                    reservation.setClassroomName(classroom.getName());
                }
        );
        return listRes;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity createReservation(@RequestBody Reservation reservation){
        Classroom classroom = classroomRepository.findById(reservation.getReservationId().getClassroomId()).get();
        if(classroom.getCapacity() < reservation.getCapacity())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        List<Reservation> reservationList = reservationRepository.findByClassroom_Id(classroom.getId());
        boolean valid = reservationList.stream().noneMatch(
                r -> r.getStartTime().isBefore(reservation.getEndTime()) && r.getEndTime().isAfter(reservation.getStartTime())
        );
        if(valid)
        {
            Reservation newRes = reservationRepository.save(reservation);
            return ResponseEntity.ok(newRes);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
