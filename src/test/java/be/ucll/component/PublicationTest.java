package be.ucll.component;

import be.ucll.repository.PublicationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class PublicationTest {

    private WebTestClient webTestClient;
    private PublicationRepository publicationRepository;

    @Autowired
    public PublicationTest(WebTestClient webTestClient, PublicationRepository publicationRepository) {
        this.webTestClient = webTestClient;
        this.publicationRepository = publicationRepository;
    }

    @AfterEach
    public void resetData() {
        publicationRepository.resetRepositoryData();
    }

    @Test
    public void givenExistingStock_whenGettingStockWithMinimumAvailableCopies_thenCorrectStockIsReturned() {
        webTestClient
                .get()
                .uri("/publications/stock/4")
                .exchange().expectStatus().isOk()
                .expectBody()
                .json("""
                  [
                    {"title":"The Catcher in the Rye","author":"J.D. Salinger","isbn":"0123456","pubYear":1951,"availableCopies":5},
                    {"title":"Gistheren","editor":"DPG Media","issn":"954789","pubYear":1925,"availableCopies":4}
                  ]
                  """);
    }
}
