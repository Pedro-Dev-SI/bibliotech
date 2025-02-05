package pedro.bibliotech.app.Services.DTOs;

import java.util.UUID;

public record BooksDTO(
        UUID id,
        String title,
        String author,
        String genre,
        int publishedYear,
        String isbn,
        boolean available
) {
}
