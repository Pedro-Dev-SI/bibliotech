package pedro.bibliotech.app.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pedro.bibliotech.app.Domain.Books;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Books, UUID> {

    Books getByTitle(String title);

    List<Books> getAllByAvailableIsTrue();
}
