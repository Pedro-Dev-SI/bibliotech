package pedro.bibliotech.app.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pedro.bibliotech.app.Domain.Books;
import pedro.bibliotech.app.Services.BookService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Books>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PostMapping("/create")
    public ResponseEntity<Books> createNewBook(@RequestBody Books book) {
        return ResponseEntity.ok(bookService.saveBook(book));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Books> getBookByTitle(@PathVariable("title") String title) {
        return ResponseEntity.ok(bookService.getBookByTitle(title));
    }

    @GetMapping("/availables")
    public ResponseEntity<List<Books>> listAllAvailable() {
        return ResponseEntity.ok(bookService.listAllAvailable());
    }

    @PostMapping("/update")
    public ResponseEntity<Books> updateBook(@RequestBody Books book) {
        return ResponseEntity.ok(bookService.updateBook(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBook(@PathVariable UUID id){
        bookService.deleteBook(id);
        return ResponseEntity.ok(HttpStatus.OK);
    };

    @PostMapping("/set-available/{id}")
    public ResponseEntity setAvailable(@PathVariable("id") UUID id) {
        bookService.setAvailable(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
