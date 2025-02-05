package pedro.bibliotech.app.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pedro.bibliotech.app.Domain.BookLoan;
import pedro.bibliotech.app.Services.BookLoanService;
import pedro.bibliotech.app.Services.DTOs.BookLoanDTO;
import pedro.bibliotech.app.Services.DTOs.CreateBookLoanDTO;
import pedro.bibliotech.app.Services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loan")
public class BookLoanController {

    private BookLoanService bookLoanService;

    private UserService userService;

    public BookLoanController(BookLoanService bookLoanService, UserService userService) {
        this.bookLoanService = bookLoanService;
        this.userService = userService;
    }

    @GetMapping("/list-all")
    public ResponseEntity<List<BookLoanDTO>> listAllLoans() {
        return ResponseEntity.ok(bookLoanService.listAllLoans());
    }

    @GetMapping("/loans-by-user/{email}")
    public ResponseEntity<List<BookLoanDTO>> listAllBookLoansByUser(@PathVariable("email") String email) {
        if (email == null) throw new RuntimeException("Email cannot be null");
        return ResponseEntity.ok(bookLoanService.listAllBookLoansByUser(email));
    }

    @GetMapping("/loans-by-current-user/active")
    public ResponseEntity<List<BookLoanDTO>> listAllBookLoansByCurrentUserActive() {
        var user = userService.getCurrentUser();

        if (user == null) {
            throw new RuntimeException("User could not be found");
        }

        return ResponseEntity.ok(bookLoanService.listAllActiveBookLoansByUser(user.getEmail()));
    }

    @GetMapping("/loans-by-current-user")
    public ResponseEntity<List<BookLoanDTO>> listAllBookLoansByCurrentUser() {
        var user = userService.getCurrentUser();

        if (user == null) {
            throw new RuntimeException("User could not be found");
        }

        return ResponseEntity.ok(bookLoanService.listAllBookLoansByUser(user.getEmail()));
    }

    @GetMapping("/list-all/overdue")
    public ResponseEntity<List<BookLoanDTO>> listAllLateLoans() {
        return ResponseEntity.ok(bookLoanService.listAllLateLoans());
    }

    @PostMapping("/make-new-loan")
    public ResponseEntity<BookLoan> makeLoan(@RequestBody CreateBookLoanDTO createBookLoanDTO) {
        return ResponseEntity.ok(bookLoanService.createNewLoan(createBookLoanDTO));
    }

    @PostMapping("/return/{id}")
    public ResponseEntity returnBookLoan(@PathVariable("id") UUID id) {
        bookLoanService.returnBookLoan(id);
        return ResponseEntity.ok().build();
    }
}
