package mk.ukim.finki.wp.kol2022.g2.service.impl;

import mk.ukim.finki.wp.kol2022.g2.model.Student;
import mk.ukim.finki.wp.kol2022.g2.repository.StudentRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LoginServiceImpl implements UserDetailsService {
    private final StudentRepository studentRepository;

    public LoginServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student emp = studentRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(username));
        return new User(
                emp.getEmail(),
                emp.getPassword(),
                Stream.of(new SimpleGrantedAuthority("ROLE_" + emp.getType())).collect(Collectors.toList())
        );
    }
}
