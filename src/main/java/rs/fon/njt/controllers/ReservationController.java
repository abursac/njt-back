package rs.fon.njt.controllers;

import com.auth0.jwt.JWT;
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

        Iterable<Reservation> listRes =  reservationRepository.getAllReservations();
        for(Reservation reservation : listRes) {
            Teacher teacher = teachersRepository.getTeacherByUsername(reservation.getTeacherUsername()).get();
            Classroom classroom = classroomRepository.findById(reservation.getClassroomId()).get();
            reservation.setFullName(teacher.getFirstName() + " " + teacher.getLastName());
            reservation.setClassroomName(classroom.getName());
        }
        return listRes;
    }

    @GetMapping("unresponded")
    @ResponseBody
    public Iterable<Reservation> getUnrespondedReservations(){

        Iterable<Reservation> listRes =  reservationRepository.findByApprovedIsNull();
        for(Reservation reservation : listRes) {
            Teacher teacher = teachersRepository.getTeacherByUsername(reservation.getTeacherUsername()).get();
            Classroom classroom = classroomRepository.findById(reservation.getClassroomId()).get();
            reservation.setFullName(teacher.getFirstName() + " " + teacher.getLastName());
            reservation.setClassroomName(classroom.getName());
        }
        return listRes;
    }

    @GetMapping("{id}")
    @ResponseBody
    public List<Reservation> getReservationsByClassroom(@PathVariable("id") Integer id)
    {
        List<Reservation> listRes =  reservationRepository.findByClassroom_Id(id);
        listRes.stream().forEach(
                reservation -> {
                    Teacher teacher = teachersRepository.getTeacherByUsername(reservation.getTeacherUsername()).get();
                    Classroom classroom = classroomRepository.findById(reservation.getClassroomId()).get();
                    reservation.setFullName(teacher.getFirstName() +" "+ teacher.getLastName());
                    reservation.setClassroomName(classroom.getName());
                }
        );
        return listRes;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity createReservation(@RequestBody Reservation reservation, @RequestHeader (name="Authorization") String token){
        token = token.substring(7);
        String username = JWT.decode(token).getSubject();
        reservation.setTeacherUsername(username);

        Classroom classroom = classroomRepository.findById(reservation.getClassroomId()).get();
        if(classroom.getCapacity() < reservation.getCapacity())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(reservation.getStartTime().isAfter(reservation.getEndTime()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        List<Reservation> reservationList = reservationRepository.findByClassroom_Id(classroom.getId());
        boolean valid = reservationList.stream().noneMatch(
                r -> r.getStartTime().isBefore(reservation.getEndTime()) && r.getEndTime().isAfter(reservation.getStartTime())
        );
        if(valid)
        {
            Reservation newRes = reservationRepository.save(reservation);
            Teacher teacher = teachersRepository.getTeacherByUsername(reservation.getTeacherUsername()).get();
            reservation.setFullName(teacher.getFirstName() +" "+ teacher.getLastName());
            return ResponseEntity.ok(newRes);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("approve/{id}")
    @ResponseBody
    public ResponseEntity approveReseravation(@PathVariable("id") Integer id)
    {
        Reservation reservation = reservationRepository.findById(id).orElse(null);

        reservation.setApproved(true);

        reservationRepository.save(reservation);

        return ResponseEntity.ok(reservation);
    }

    @PostMapping("decline/{id}")
    @ResponseBody
    public ResponseEntity deleteReseravation(@PathVariable("id") Integer id)
    {
        Reservation reservation = reservationRepository.findById(id).orElse(null);

        reservation.setApproved(false);

        reservationRepository.save(reservation);

        return ResponseEntity.ok(reservation);
    }
}
