package pedro.bibliotech.app.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pedro.bibliotech.app.Domain.BookLoan;
import pedro.bibliotech.app.Domain.Books;
import pedro.bibliotech.app.Domain.User;
import pedro.bibliotech.app.Enums.RolesEnum;
import pedro.bibliotech.app.Exceptions.LateLoanException;
import pedro.bibliotech.app.Repositories.BookLoanRepository;
import pedro.bibliotech.app.Repositories.BookRepository;
import pedro.bibliotech.app.Repositories.UserRepository;
import pedro.bibliotech.app.Services.DTOs.CreateBookLoanDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class BookLoanServiceTest {
    @Mock
    private BookLoanRepository bookLoanRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookLoanService bookLoanService;

    private User user;
    private Books book;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(UUID.randomUUID(), "user@example.com", "123456789", RolesEnum.valueOf("ROLE_ADMIN"), "John", "Doe", "35998766374", "154.198.176-18", "Rua Leste, 65");
        book = new Books(
                UUID.randomUUID(),
                "Book Title",
                5,
                "Author Name",  // author
                "Fiction",      // genre
                2021,           // publishedYear
                "978-3-16-148410-0" // isbn
        );
    }

    @Test
    public void testCreateNewLoan_withLateLoans() {
        CreateBookLoanDTO loanRequest = new CreateBookLoanDTO("user@example.com", List.of(book.getId()));
        BookLoan lateLoan = new BookLoan(UUID.randomUUID(), user, Set.of(book), LocalDate.now().minusDays(5), LocalDate.now().minusDays(1), null);

        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(bookLoanRepository.findAllByUserId(user.getId())).thenReturn(List.of(lateLoan));

        assertThrows(LateLoanException.class, () -> {
            bookLoanService.createNewLoan(loanRequest);
        });
    }

    @Test
    public void testCreateNewLoan_success() {
        CreateBookLoanDTO loanRequest = new CreateBookLoanDTO("user@example.com", List.of(book.getId()));

        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(bookLoanRepository.findAllByUserId(user.getId())).thenReturn(List.of());
        when(bookRepository.findAllById(anyList())).thenReturn(List.of(book));

        BookLoan createdLoan = new BookLoan(UUID.randomUUID(), user, Set.of(book), LocalDate.now(), LocalDate.now().plusDays(10), null);
        when(bookLoanRepository.save(any(BookLoan.class))).thenReturn(createdLoan);

        BookLoan result = bookLoanService.createNewLoan(loanRequest);

        assertNotNull(result);
        assertEquals(user.getId(), result.getUser().getId());
    }
}