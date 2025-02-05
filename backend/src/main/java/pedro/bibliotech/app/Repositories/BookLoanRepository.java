package pedro.bibliotech.app.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pedro.bibliotech.app.Domain.BookLoan;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, UUID> {

    List<BookLoan> findAllByUserId(UUID userId);

    List<BookLoan> findAllByUserIdAndReturnDateIsNull(UUID userId);

    List<BookLoan> findByDueDateBeforeAndReturnDateIsNull(LocalDate today);
}
