package yubackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yubackend.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    Optional<User> findById(Long aLong);
    User findByUsername(String username);

}
