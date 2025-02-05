package pedro.bibliotech.app.Services;

import org.springframework.stereotype.Service;
import pedro.bibliotech.app.Domain.Books;
import pedro.bibliotech.app.Repositories.BookRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Books> getAllBooks() {
        return bookRepository.findAll();
    }

    public Books saveBook(Books book) {
        return bookRepository.save(book);
    }

    public Books getBookByTitle(String title){return bookRepository.getByTitle(title);}

    public Books updateBook(Books book) {

        if (book.getQntAvailable() == 0) {
            book.setAvailable(false);
        }

        return bookRepository.save(book);
    };

    public void setAvailable(UUID id) {
        Books book = bookRepository.findById(id).get();

        if (book != null) {
            book.setAvailable(!book.isAvailable());
            bookRepository.save(book);
        }

    }

    public void deleteBook(UUID id) {
        bookRepository.deleteById(id);
    }

    public List<Books> listAllAvailable() {
        return bookRepository.getAllByAvailableIsTrue();
    }

    public void updateBookQntAvailable(Set<Books> books) {
        var listBooks = books.stream().collect(Collectors.toList());

        listBooks.forEach(book -> {
            book.setQntAvailable(book.getQntAvailable() + 1);
        });

        bookRepository.saveAll(listBooks);
    }
}
