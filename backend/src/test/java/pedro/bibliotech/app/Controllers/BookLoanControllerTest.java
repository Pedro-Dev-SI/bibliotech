package pedro.bibliotech.app.Controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pedro.bibliotech.app.Domain.Books;
import pedro.bibliotech.app.Domain.User;
import pedro.bibliotech.app.Enums.RolesEnum;
import pedro.bibliotech.app.Services.BookLoanService;
import pedro.bibliotech.app.Services.DTOs.BookLoanDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BookLoanControllerTest {

    @Mock
    private BookLoanService bookLoanService;

    @InjectMocks
    private BookLoanController bookLoanController;

    private User user;
    private Books book;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Criando a instância do User com todos os parâmetros exigidos pelo construtor
        user = new User(
                UUID.randomUUID(),
                "user@example.com",
                "encryptedPassword",
                RolesEnum.ROLE_ADMIN,  // Supondo que você tenha um Enum com os papéis
                "John",
                "Doe",
                "35998766374",
                "154.198.176-18",
                "Rua Leste, 65"
        );

        // Criando a instância do Books
        book = new Books(
                UUID.randomUUID(),
                "Book Title",
                5,
                "Author Name",
                "Genre",
                2020,
                "1234567890123"
        );
    }

    // Agora você pode continuar criando os testes como mockar os métodos da controller, por exemplo:

    @Test
    public void testListAllLoans() {
        // Mock para o serviço
        List<BookLoanDTO> mockLoans = new ArrayList<>();
        when(bookLoanService.listAllLoans()).thenReturn(mockLoans);

        // Realizando o teste
        ResponseEntity<List<BookLoanDTO>> response = bookLoanController.listAllLoans();

        // Verificando a resposta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockLoans, response.getBody());
    }
}