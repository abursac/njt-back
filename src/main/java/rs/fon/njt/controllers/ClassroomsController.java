package rs.fon.njt.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.fon.njt.models.Classroom;
import rs.fon.njt.models.Teacher;
import rs.fon.njt.repository.ClassroomRepository;

@RestController
@RequestMapping("classrooms")
public class ClassroomsController {
    @Autowired
    private ClassroomRepository classroomRepository;

    @GetMapping
    public Iterable<Classroom> getClassrooms(){
        return classroomRepository.findAll();
    }

    @GetMapping("{id}")
    @ResponseBody
    public Classroom getClassroom(@PathVariable("id") Integer id){
        return classroomRepository.findById(id).get();
    }

    @PostMapping
    public ResponseEntity<Classroom> addClassroom(@RequestBody Classroom classroom){
        Classroom c = classroomRepository.save(classroom);
        return ResponseEntity.ok(c);
    }

}
