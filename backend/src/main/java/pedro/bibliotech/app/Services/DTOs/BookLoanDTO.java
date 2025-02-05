package pedro.bibliotech.app.Services.DTOs;

import pedro.bibliotech.app.Domain.BookLoan;
import pedro.bibliotech.app.Domain.Books;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record BookLoanDTO(

        UUID id,
        String userEmail,
        String userName,
        String userCpf,
        List<Books> books,
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate
) {
    public static BookLoanDTO from(BookLoan bookLoan) {
        return new BookLoanDTO(
                bookLoan.getId(),
                bookLoan.getUser().getEmail(),
                bookLoan.getUser().getFirstName() + " " + bookLoan.getUser().getLastName(),
                bookLoan.getUser().getCpf(),
                bookLoan.getBooks().stream().toList(),
                bookLoan.getLoanDate(),
                bookLoan.getDueDate(),
                bookLoan.getReturnDate()
        );
    }

    public static List<BookLoanDTO> from(List<BookLoan> bookLoans) {
        return bookLoans.stream()
                .map(bookLoan -> new BookLoanDTO(
                        bookLoan.getId(),
                        bookLoan.getUser().getEmail(),
                        bookLoan.getUser().getFirstName() + " " + bookLoan.getUser().getLastName(),
                        bookLoan.getUser().getCpf(),
                        bookLoan.getBooks().stream().toList(),
                        bookLoan.getLoanDate(),
                        bookLoan.getDueDate(),
                        bookLoan.getReturnDate()
                ))
                .toList(); // Retorna uma lista de BookLoanDTO
    }

}
