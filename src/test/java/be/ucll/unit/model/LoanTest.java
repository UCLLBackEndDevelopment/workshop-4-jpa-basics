package be.ucll.unit.model;

import be.ucll.model.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private User validUser;
    private Book validBook, bookWithNoCopies;
    private Magazine validMagazine;
    private List<Publication> validPublications, listWithBookWithNoCopies;
    private LocalDate validStartDate, validEndDate;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @BeforeEach
    public void setUp() {
        validUser = new User("Amelia", 15, "amelia@ucll.be", "amelia1234");
        validBook = new Book("The Hobbit", "J.R.R. Tolkien", "978-0-261-10295-2", 1937, 4);
        validMagazine = new Magazine("National Geographic", "National Geographic Society", "978-1-4262-0034-5", 1888, 4);
        validPublications = List.of(validBook, validMagazine);

        bookWithNoCopies = new Book("The Hobbit", "J.R.R. Tolkien", "978-0-261-10295-2", 1937, 1);
        bookWithNoCopies.lendPublication();

        listWithBookWithNoCopies = List.of(bookWithNoCopies, validMagazine);

        validStartDate = LocalDate.now().minusYears(2);
        validEndDate = validStartDate.plusDays(21);
    }

    @Test
    public void givenValidInput_whenLoanIsCreated_thenAllFieldsHaveCorrectValues() {
        Loan loan = new Loan(validUser, validPublications, validStartDate);
        assertEquals(validUser, loan.getUser());
        assertEquals(validPublications, loan.getPublications());
        assertEquals(validStartDate, loan.getStartDate());
        assertEquals(validEndDate, loan.getEndDate());
        assertEquals(3, validBook.getAvailableCopies());
        assertEquals(3, validMagazine.getAvailableCopies());
    }

    @Test
    public void givenListWithBookWithNoCopies_whenLoanIsCreated_thenErrorIsThrownAndMagazineIsNotRented() {
        Exception ex = Assertions.assertThrows(
                RuntimeException.class,
                () -> new Loan(validUser, listWithBookWithNoCopies, validStartDate));
        assertEquals("Unable to lend publication. No copies available for The Hobbit", ex.getMessage());
        assertEquals(4, validMagazine.getAvailableCopies());
        assertEquals(0, bookWithNoCopies.getAvailableCopies());
    }

    @Test
    public void givenInvalidEndOrStartDate_whenLoanIsCreated_thenErrorIsThrown() {
        Set<ConstraintViolation<Loan>> violations = validator.validate(new Loan(validUser, validPublications, null));
        assertEquals(1, violations.size());
        ConstraintViolation<Loan> violation = violations.iterator().next();
        assertEquals("Start date is required", violation.getMessage());

        Exception ex = Assertions.assertThrows(RuntimeException.class,
                () -> new Loan(validUser, validPublications, LocalDate.now().plusDays(1)));

        assertEquals("Start date cannot be in the future", ex.getMessage());
    }

    @Test
    public void givenEmptyListOfPublication_whenLoanIsCreated_thenErrorIsThrown() {
        Exception ex = Assertions.assertThrows(
                RuntimeException.class,
                () -> new Loan(validUser, List.of(), validStartDate));

        assertEquals("List is required", ex.getMessage());
    }

    @Test
    public void givenValidInput_whenLoanIsCreated_thenTheNumberOfAvailableCopiesIsDecreased() {
        // given
        assertEquals(4, validBook.getAvailableCopies());
        assertEquals(4, validMagazine.getAvailableCopies());
        // when
        Loan loan = new Loan(validUser, validPublications, validStartDate);
        // then
        assertEquals(3, validBook.getAvailableCopies());
        assertEquals(3, validMagazine.getAvailableCopies());
    }

    @Test
    public void givenLoanWithPublication_whenPublicationsAreReturned_thenAvailableCopiesAreIncreased() {
        // given
        Loan loan = new Loan(validUser, validPublications, validStartDate);
        assertEquals(3, validBook.getAvailableCopies());
        assertEquals(3, validMagazine.getAvailableCopies());
        // when
        loan.returnPublications();
        // then
        assertEquals(4, validBook.getAvailableCopies());
        assertEquals(4, validMagazine.getAvailableCopies());
    }
}
