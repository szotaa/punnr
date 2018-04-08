package pl.szotaa.punnr.user.repository;

import org.springframework.data.repository.CrudRepository;
import pl.szotaa.punnr.user.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
