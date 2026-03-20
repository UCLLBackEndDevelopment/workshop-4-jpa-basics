package be.ucll.repository;

import be.ucll.model.Loan;
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
        loans = new ArrayList<>(List.of(new Loan(new UserRepository().getUsers().get(0),
                        List.of(new PublicationRepository().getBooks().get(0)), LocalDate.now()),
                new Loan(new UserRepository().getUsers().get(1),
                        List.of(new PublicationRepository().getBooks().get(0)),
                        LocalDate.now()),
                new Loan(new UserRepository().getUsers().get(1),
                        List.of(new PublicationRepository().getBooks().get(1)),
                        LocalDate.now().minusDays(5)),
                new Loan(new UserRepository().getUsers().get(0),
                        List.of(new PublicationRepository().getBooks().get(1)),
                        LocalDate.now())));
    }

    public List<Loan> getLoansByUser(String email, boolean onlyActive) {
        return loans.stream()
                .filter(loan -> loan.getUser().getEmail().equals(email))
                .filter(loan -> !onlyActive || loan.getStartDate().isBefore(LocalDate.now()) && LocalDate.now().isBefore(loan.getEndDate()))
                .toList();
    }

    public void deleteLoansByUserEmail(String email) {
        loans.removeIf(loan -> loan.getUser().getEmail().equals(email));
    }
}
