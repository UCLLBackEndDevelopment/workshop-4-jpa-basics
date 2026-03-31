package be.ucll.repository;

import be.ucll.model.Loan;
import be.ucll.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LoanRepository {

    public List<Loan> loans;

    public LoanRepository() {
        resetRepositoryData();
    }

    public void resetRepositoryData() {
        ArrayList<User> users = new ArrayList<>(List.of(
                new User("John Doe", 25, "john.doe@ucll.be", "john1234"),
                new User("Jane Toe", 30, "jane.toe@ucll.be", "jane1234"),
                new User("Jack Doe", 5, "jack.doe@ucll.be", "jack1234"),
                new User("Sarah Doe", 4, "sarah.doe@ucll.be", "sarah1234"),
                new User("Birgit Doe", 18, "birgit.doe@ucll.be", "birgit1234")
        ));

        loans = new ArrayList<>(List.of(new Loan(users.get(0),
                        List.of(new PublicationRepository().getBooks().get(0)), LocalDate.now().minusDays(22)),
                new Loan(users.get(1),
                        List.of(new PublicationRepository().getBooks().get(0)),
                        LocalDate.now().minusDays(22)),
                new Loan(users.get(1),
                        List.of(new PublicationRepository().getBooks().get(1)),
                        LocalDate.now().minusDays(5)),
                new Loan(users.get(0),
                        List.of(new PublicationRepository().getBooks().get(1)),
                        LocalDate.now().minusDays(22))));
    }

    public List<Loan> getLoansByUser(String email, boolean onlyActive) {
        return loans.stream()
                .filter(loan -> loan.getUser().getEmail().equals(email))
                .filter(loan -> !onlyActive || ((loan.getStartDate().isBefore(LocalDate.now()) || loan.getStartDate().equals(LocalDate.now()))
                        && (LocalDate.now().isBefore(loan.getEndDate()) || LocalDate.now().equals(loan.getEndDate()))))
                .toList();
    }

    public void deleteLoansByUserEmail(String email) {
        loans.removeIf(loan -> loan.getUser().getEmail().equals(email));
    }
}
