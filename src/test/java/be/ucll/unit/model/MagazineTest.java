package be.ucll.unit.model;

import be.ucll.model.Book;
import be.ucll.model.Magazine;
import be.ucll.model.Publication;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MagazineTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void givenValidValues_whenMagazineIsCreated_thenMagazineIsCreatedWithThoseValues() {
        Magazine magazine = new Magazine(
                "National Geographic",
                "National Geographic Society",
                "978-1-4262-0034-5",
                1888, 4);

        Assertions.assertEquals("National Geographic", magazine.getTitle());
        Assertions.assertEquals("National Geographic Society", magazine.getEditor());
        Assertions.assertEquals("978-1-4262-0034-5", magazine.getIssn());
        Assertions.assertEquals(1888, magazine.getPubYear());
        Assertions.assertEquals(4, magazine.getAvailableCopies());
    }

    @Test
    public void givenInvalidTitle_whenMagazineIsCreated_thenErrorIsThrown() {
        Set<ConstraintViolation<Magazine>> violations = validator.validate(new Magazine(null, "National Geographic Society", "978-1-4262-0034-5", 1888, 4));
        assertEquals(1, violations.size());
        ConstraintViolation<Magazine> violation = violations.iterator().next();
        assertEquals("Title is required", violation.getMessage());

        violations = validator.validate(new Magazine("", "National Geographic Society", "978-1-4262-0034-5", 1888, 4));
        assertEquals(1, violations.size());
        violation = violations.iterator().next();
        assertEquals("Title is required", violation.getMessage());
    }

    @Test
    public void givenInvalidAuthor_whenMagazineIsCreated_thenErrorIsThrown() {
        Set<ConstraintViolation<Magazine>> violations = validator.validate(new Magazine("National Geographic", null, "978-1-4262-0034-5", 1888, 4));
        assertEquals(1, violations.size());
        ConstraintViolation<Magazine> violation = violations.iterator().next();
        assertEquals("Editor is required", violation.getMessage());

        violations = validator.validate(new Magazine("National Geographic", "", "978-1-4262-0034-5", 1888, 4));
        assertEquals(1, violations.size());
        violation = violations.iterator().next();
        assertEquals("Editor is required", violation.getMessage());
    }

    @Test
    public void givenInvalidISBN_whenMagazineIsCreated_thenErrorIsThrown() {
        Set<ConstraintViolation<Magazine>> violations = validator.validate(new Magazine("National Geographic", "National Geographic Society", null, 1888, 4));
        assertEquals(1, violations.size());
        ConstraintViolation<Magazine> violation = violations.iterator().next();
        assertEquals("ISSN is required", violation.getMessage());

        violations = validator.validate(new Magazine("National Geographic", "National Geographic Society", "", 1888, 4));
        assertEquals(1, violations.size());
        violation = violations.iterator().next();
        assertEquals("ISSN is required", violation.getMessage());
    }

    @Test
    public void givenInvalidYear_whenMagazineIsCreated_thenErrorIsThrown() {
        Set<ConstraintViolation<Magazine>> violations = validator.validate(new Magazine("National Geographic", "National Geographic Society", "978-1-4262-0034-5", -1888, 4));
        assertEquals(1, violations.size());
        ConstraintViolation<Magazine> violation = violations.iterator().next();
        assertEquals("Publication year must be a positive integer", violation.getMessage());

        Exception ex = Assertions.assertThrows(
                RuntimeException.class,
                () -> new Magazine(
                        "National Geographic",
                        "National Geographic Society",
                        "978-1-4262-0034-5",
                        Year.now().getValue() + 1, 4));

        Assertions.assertEquals("Publication year cannot be in the future", ex.getMessage());
    }

    @Test
    public void givenInvalidAvailableCopies_whenMagazineIsCreated_thenErrorIsThrown() {
        Set<ConstraintViolation<Magazine>> violations = validator.validate(new Magazine("National Geographic", "National Geographic Society", "978-1-4262-0034-5", 1888, -4));
        assertEquals(1, violations.size());
        ConstraintViolation<Magazine> violation = violations.iterator().next();
        assertEquals("Publication must have at least one copy", violation.getMessage());
    }

    @Test
    public void givenValidMagazineWith2AvailableCopies_whenLendingOneCopy_thenOneCopyIsLent() {
        Magazine magazine = new Magazine(
                "National Geographic",
                "National Geographic Society",
                "978-1-4262-0034-5",
                1888, 2);

        Publication lendedMagazine = magazine.lendPublication();
        Assertions.assertEquals(1, magazine.getAvailableCopies());
        Assertions.assertEquals(magazine, lendedMagazine);
    }

    @Test
    public void givenValidMagazineWithNoAvailableCopies_whenLendingOneCopy_thenErrorIsThrown() {
        Magazine magazine = new Magazine(
                "National Geographic",
                "National Geographic Society",
                "978-1-4262-0034-5",
                1888, 1);
        magazine.lendPublication(); // lend the one available copy

        Exception ex = Assertions.assertThrows(
                RuntimeException.class,
                magazine::lendPublication);

        Assertions.assertEquals("No available copies left for publication", ex.getMessage());

    }

    @Test
    public void givenValidMagazineWith2AvailableCopies_whenReturningOneCopy_thenOneCopyIsReturned() {
        Magazine magazine = new Magazine(
                "National Geographic",
                "National Geographic Society",
                "978-1-4262-0034-5",
                1888, 2);

        magazine.returnPublication();
        Assertions.assertEquals(3, magazine.getAvailableCopies());
    }
}
