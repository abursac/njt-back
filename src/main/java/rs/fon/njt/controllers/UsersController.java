package rs.fon.njt.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rs.fon.njt.models.*;
import rs.fon.njt.repository.AdminsRepository;
import rs.fon.njt.repository.TeachersRepository;
import rs.fon.njt.repository.UsersRepository;

import java.util.Optional;


@RestController
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private TeachersRepository teachersRepository;
    @Autowired
    private AdminsRepository adminsRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginData loginData){
        User user = usersRepository.findById(loginData.getUsername()).orElse(null);
        if(user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if(!passwordEncoder.matches(loginData.getPassword(), user.getPassword()))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String token = JWT.create().withSubject(loginData.getUsername())
                .withClaim("role", user.getRole())
                .sign(Algorithm.HMAC256("secret"));
        return ResponseEntity.ok(new Token(token));
    }

    @GetMapping("teachers")
    public Iterable<Teacher> getTeachers(){
        return teachersRepository.findAll();
    }

    @GetMapping("currentUser")
    @ResponseBody
    public User getUser(@RequestHeader (name="Authorization") String token){
        token = token.substring(7);
        String username = JWT.decode(token).getSubject();
        return usersRepository.findById(username).get();
    }

    @PostMapping("teacher")
    public ResponseEntity<Teacher> addTeacher(@RequestBody Teacher teacher){
        String encodedPassword = passwordEncoder.encode(teacher.getPassword());
        teacher.setPassword(encodedPassword);
        Teacher t = teachersRepository.save(teacher);
        return ResponseEntity.ok(t);
    }

    @GetMapping("admins")
    public Iterable<Admin> getAdmins(){
        return adminsRepository.findAll();
    }

    @GetMapping("admins/{username}")
    @ResponseBody
    public Admin getAdmin(@PathVariable("username") String username){
        return adminsRepository.findById(username).get();
    }

    @PostMapping("admin")
    public void addAdmin(@RequestBody Admin admin){
        adminsRepository.save(admin);
    }
}
