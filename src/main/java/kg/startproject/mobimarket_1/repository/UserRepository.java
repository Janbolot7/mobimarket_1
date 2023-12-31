package kg.startproject.mobimarket_1.repository;

import kg.startproject.mobimarket_1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(int id);
    User findByPhoneNumber(String phoneNumber);
    Optional<User> findFirstByUsername(String username);
    Optional<User> findFirstByEmail(String email);
}
