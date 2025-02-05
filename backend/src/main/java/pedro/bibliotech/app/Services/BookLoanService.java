package pedro.bibliotech.app.Services;

import org.springframework.stereotype.Service;
import pedro.bibliotech.app.Domain.BookLoan;
import pedro.bibliotech.app.Domain.Books;
import pedro.bibliotech.app.Domain.User;
import pedro.bibliotech.app.Exceptions.LateLoanException;
import pedro.bibliotech.app.Repositories.BookLoanRepository;
import pedro.bibliotech.app.Repositories.BookRepository;
import pedro.bibliotech.app.Repositories.UserRepository;
import pedro.bibliotech.app.Services.DTOs.BookLoanDTO;
import pedro.bibliotech.app.Services.DTOs.CreateBookLoanDTO;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookLoanService {

    private BookLoanRepository bookLoanRepository;
    private UserRepository userRepository;

    private BookRepository bookRepository;

    private BookService bookService;

    public BookLoanService(BookLoanRepository bookLoanRepository, UserRepository userRepository, BookRepository bookRepository, BookService bookService) {
        this.bookLoanRepository = bookLoanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    public List<BookLoanDTO> listAllLoans() {
        var bookLoans = bookLoanRepository.findAll();
        return BookLoanDTO.from(bookLoans);
    }

    public List<BookLoanDTO> listAllBookLoansByUser(String email) {
        User user = userRepository.findByEmail(email);
        var bookLoans = bookLoanRepository.findAllByUserId(user.getId());
        return BookLoanDTO.from(bookLoans);
    };

    public List<BookLoanDTO> listAllActiveBookLoansByUser(String email) {
        User user = userRepository.findByEmail(email);
        var bookLoans = bookLoanRepository.findAllByUserIdAndReturnDateIsNull(user.getId());
        return BookLoanDTO.from(bookLoans);
    };

    public List<BookLoan> getLoansByUser(UUID userId) {
        return bookLoanRepository.findAllByUserId(userId);
    }

    public BookLoan createNewLoan(CreateBookLoanDTO createBookLoanDTO) {

        //VERIFY IF THE USER HAS LATE LOANS
        User user = userRepository.findByEmail(createBookLoanDTO.email());
        List<BookLoan> bookLoansByUser = this.getLoansByUser(user.getId());

        if (this.verifyLateLoans(bookLoansByUser)) {
            throw new LateLoanException("Já existe empréstimos pendentes para esse usuário");
        }

        List<Books> books = bookRepository.findAllById(createBookLoanDTO.booksIds());

        BookLoan bookLoanToSave = new BookLoan();

        bookLoanToSave.setUser(user);
        bookLoanToSave.setBooks(books.stream().collect(Collectors.toSet()));
        bookLoanToSave.setLoanDate(LocalDate.now(ZoneId.of("America/Sao_Paulo")));
        bookLoanToSave.setDueDate(LocalDate.now(ZoneId.of("America/Sao_Paulo")).plusDays(10));

        var bookLoanSaved = bookLoanRepository.save(bookLoanToSave);

        books.forEach(book -> {
            book.setQntAvailable(book.getQntAvailable() - 1);
            bookService.updateBook(book);
        });

        return bookLoanSaved;

    }

    public Boolean verifyLateLoans(List<BookLoan> bookLoans) {

        LocalDate actualDate = LocalDate.now(ZoneId.of("America/Sao_Paulo"));

        boolean hasLateLoans = false;

        for (var bookLoan : bookLoans) {
            if (bookLoan.getDueDate().isBefore(actualDate)) {
                hasLateLoans = true;
            }
        }

        return hasLateLoans;
    }

    public List<BookLoan> checkForOverdueLoans() {
        LocalDate today = LocalDate.now();
        return bookLoanRepository.findByDueDateBeforeAndReturnDateIsNull(today);
    }

    public void returnBookLoan(UUID id) {
        BookLoan bookLoanToBeReturned = bookLoanRepository.findById(id).get();
        bookLoanToBeReturned.setReturnDate(LocalDate.now(ZoneId.of("America/Sao_Paulo")));
        bookLoanRepository.save(bookLoanToBeReturned);

        bookService.updateBookQntAvailable(bookLoanToBeReturned.getBooks());
    }

    public List<BookLoanDTO> listAllLateLoans() {
        var bookLoans = this.checkForOverdueLoans();
        return BookLoanDTO.from(bookLoans);
    }
}
