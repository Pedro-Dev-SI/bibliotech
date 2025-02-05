package pedro.bibliotech.app.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "books", schema = "public")
@AllArgsConstructor
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "genre")
    private String genre;

    @Column(name = "published_year")
    private int publishedYear;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "available")
    private boolean available = true;

    @Column(name = "qnt_available", nullable = false)
    private Integer qntAvailable;

    public Books() {

    }

    public Books(UUID randomUUID, String book_title, int i, String author, String genre, int publishedYear, String isbn) {
        this.id = randomUUID;
        this.title = book_title;
        this.qntAvailable = i;
        this.author = author;
        this.genre = genre;
        this.publishedYear = publishedYear;
        this.isbn = isbn;
    }
}
