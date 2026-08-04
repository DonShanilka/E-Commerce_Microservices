package lk.ijse.userservice.repository;

import lk.ijse.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findByEmail(String email);

}