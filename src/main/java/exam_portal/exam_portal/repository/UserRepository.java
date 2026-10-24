package exam_portal.exam_portal.repository;

import exam_portal.exam_portal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Used during login to fetch a user by email, and during
    // registration to check for duplicates.
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}