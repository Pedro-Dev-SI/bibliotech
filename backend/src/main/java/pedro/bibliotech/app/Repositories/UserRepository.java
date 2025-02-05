package pedro.bibliotech.app.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pedro.bibliotech.app.Domain.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByEmail(String email);
}
