package pedro.bibliotech.app.Services.DTOs;

import java.util.List;
import java.util.UUID;

public record CreateBookLoanDTO(

        String email,
        List<UUID> booksIds

) {
}
