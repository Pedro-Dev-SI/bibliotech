package pedro.bibliotech.app.Jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import pedro.bibliotech.app.Domain.BookLoan;
import pedro.bibliotech.app.Services.BookLoanService;

import java.util.List;

public class CheckDueDateSchedule {

    @Autowired
    private BookLoanService bookLoanService;

    @Autowired
    private JavaMailSender mailSender;

    // Este método será executado todos os dias às 15h (hora do servidor)
    @Scheduled(cron = "0 0 15 * * ?")
    public void checkOverdueLoansAndSendEmails() {
        List<BookLoan> overdueLoans = bookLoanService.checkForOverdueLoans();

        if (!overdueLoans.isEmpty()) {
            for (BookLoan bookLoan : overdueLoans) {
                sendEmailToUser(bookLoan);
            }
        }
    }

    private void sendEmailToUser(BookLoan bookLoan) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(bookLoan.getUser().getEmail());
        message.setSubject("Empréstimo de livro atrasado");
        message.setText("Olá " + bookLoan.getUser().getFirstName() + ",\n\n" +
                "Seu empréstimo de livro está atrasado. Por favor, devolva o livro o mais rápido possível.\n\n" +
                "Detalhes do empréstimo:\n" +
                "Data de empréstimo: " + bookLoan.getLoanDate() + "\n" +
                "Data de vencimento: " + bookLoan.getDueDate() + "\n\n" +
                "Atenciosamente,\nBibliotech");

        mailSender.send(message);
    }
}
