package mk.ukim.finki.wp.kol2022.g2.repository;

import mk.ukim.finki.wp.kol2022.g2.model.Course;
import mk.ukim.finki.wp.kol2022.g2.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    Optional<Student> findByEmail(String email);
    List<Student> findByEnrollmentDateBeforeAndCoursesContaining(LocalDate before, Course course);

    List<Student> findByCoursesContaining(Course skill);

    List<Student> findByEnrollmentDateBefore(LocalDate employmentBefore);
}
