package be.ucll.controller;

import be.ucll.model.Publication;
import be.ucll.service.PublicationService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/publications")
public class PublicationRestController {

    private PublicationService publicationService;

    public PublicationRestController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @GetMapping
    public List<Publication> filterPublicationsByTitleAndType(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String type) {

        return publicationService.filterPublicationsByTitleAndType(title, type);
    }

    @GetMapping("stock/{availableCopies}")
    public List<Publication> getPublicationsWithMinimalNumberOfCopies(@PathVariable int availableCopies) {
        return publicationService.getPublicationsWithMinimalNumberOfCopies(availableCopies);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({RuntimeException.class})
    public Map<String, String> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getFieldErrors()) {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }
        return errors;
    }
}
