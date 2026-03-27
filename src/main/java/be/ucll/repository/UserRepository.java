package be.ucll.repository;

import be.ucll.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByAgeGreaterThan(int age);

    List<User> findByAgeBetween(int minAge, int maxAge);

    List<User> findByName(String name);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findAllByOrderByAgeDesc();

    List<User> findByNameContainingAndAgeGreaterThan(String chars, int age);

}
