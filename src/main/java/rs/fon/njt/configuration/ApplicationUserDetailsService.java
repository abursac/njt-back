package rs.fon.njt.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.fon.njt.models.Teacher;
import rs.fon.njt.repository.TeachersRepository;

import java.util.Collections;
import java.util.Optional;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {
    @Autowired
    private TeachersRepository teachersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Teacher> teacher = teachersRepository.findById(username);
        if(!teacher.isPresent())
            throw new UsernameNotFoundException(username);
        return new User(teacher.get().getUsername(), teacher.get().getPassword(), Collections.emptyList());
    }
}
