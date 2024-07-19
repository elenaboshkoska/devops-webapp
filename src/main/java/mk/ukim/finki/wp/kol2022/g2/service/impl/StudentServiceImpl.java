package mk.ukim.finki.wp.kol2022.g2.service.impl;

import mk.ukim.finki.wp.kol2022.g2.model.Course;
import mk.ukim.finki.wp.kol2022.g2.model.Student;
import mk.ukim.finki.wp.kol2022.g2.model.StudentType;
import mk.ukim.finki.wp.kol2022.g2.model.exceptions.InvalidCourseIdException;
import mk.ukim.finki.wp.kol2022.g2.model.exceptions.InvalidStudentIdException;
import mk.ukim.finki.wp.kol2022.g2.repository.CourseRepository;
import mk.ukim.finki.wp.kol2022.g2.repository.StudentRepository;
import mk.ukim.finki.wp.kol2022.g2.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Student> listAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(InvalidStudentIdException::new);
    }

    @Override
    public Student create(String name, String email, String password, StudentType type, List<Long> courseId, LocalDate enrollmentDate) {
        List<Course> courses = courseId.stream().map(course -> courseRepository.findById(course).orElseThrow(InvalidCourseIdException::new)).collect(Collectors.toList());
        return studentRepository.save(new Student(name,email,passwordEncoder.encode(password),type,courses,enrollmentDate));
    }

    @Override
    public Student update(Long id, String name, String email, String password, StudentType type, List<Long> coursesId, LocalDate enrollmentDate) {
        Student found = studentRepository.findById(id).orElseThrow(InvalidStudentIdException::new);
        List<Course> courses = coursesId.stream().map(course -> courseRepository.findById(course).orElseThrow(InvalidCourseIdException::new)).collect(Collectors.toList());
        found.setName(name);
        found.setEmail(email);
        found.setPassword(passwordEncoder.encode(password));
        found.setType(type);
        found.setCourses(courses);
        found.setEnrollmentDate(enrollmentDate);
        studentRepository.save(found);
        return found;
    }

    @Override
    public Student delete(Long id) {
        Student found = studentRepository.findById(id).orElseThrow(InvalidStudentIdException::new);
        studentRepository.delete(found);
        return found;
    }

    @Override
    public List<Student> filter(Long courseId, Integer yearsOfStudying) {
        if (courseId != null && yearsOfStudying != null) {
            Course course = courseRepository.findById(courseId).orElseThrow(InvalidCourseIdException::new);
            LocalDate employmentBefore = LocalDate.now().minusYears(yearsOfStudying);
            return studentRepository.findByEnrollmentDateBeforeAndCoursesContaining(employmentBefore, course);
        } else if (courseId == null && yearsOfStudying == null) {
            return studentRepository.findAll();
        } else if (courseId != null) {
            Course skill = courseRepository.findById(courseId).orElseThrow(InvalidCourseIdException::new);
            return studentRepository.findByCoursesContaining(skill);
        } else {
            LocalDate employmentBefore = LocalDate.now().minusYears(yearsOfStudying);
            return studentRepository.findByEnrollmentDateBefore(employmentBefore);
        }
    }
}
