package example.cashcard;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CashcardApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@BeforeEach
	public void setupRestTemplate() {
		restTemplate = restTemplate.withBasicAuth("test", "pass");
	}

	@Test
	@DisplayName("Testa busca do recurso CashCard")
	void shouldReturnACashCardWhenDataIsSaved() {
		ResponseEntity<String> response = restTemplate.getForEntity("/cashcards/99", String.class);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

		DocumentContext context = JsonPath.parse(response.getBody());
		Number id = context.read("$.id");
		Assertions.assertEquals(99L, id.longValue());
		Number amount = context.read("$.amount");
		Assertions.assertEquals(123.45, amount.doubleValue());
	}

	@Test
	@DisplayName("Testa falha na busca do recurso CashCard")
	void shouldNotReturnACashCardWithAnUnknownId() {
		ResponseEntity<String> response = restTemplate.getForEntity("/cashcards/1000", String.class);

		Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		Assertions.assertTrue(Optional.ofNullable(response.getBody()).orElse("").isBlank());
	}

	@Test
	@DisplayName("Testa criacao do recurso CashCard")
	void shouldReturnIntoLocationHeaderGetEndpointToTheCashCardCreated() {
		ResponseEntity<String> response = restTemplate
				.postForEntity("/cashcards", new CashCardRequest(123.45), String.class);

		Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
		Assertions.assertNull(response.getBody());
		Assertions.assertTrue(
				Objects.requireNonNull(response.getHeaders().getLocation()).getPath().matches("/cashcards/\\d+"));
	}

	@Test
	@DisplayName("Testa busca paginada e ordenada de CashCards")
	void shouldReturnPaginatedCashCardsSaved() {
		ResponseEntity<String> response = restTemplate
				.getForEntity("/cashcards?page=1&size=3&sort=amount,desc", String.class);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

		DocumentContext context = JsonPath.parse(response.getBody());
		Number elements = context.read("$.elements");
		Assertions.assertEquals(5, elements);
		Number pages = context.read("$.pages");
		Assertions.assertEquals(2, pages);
		Number size = context.read("$.cashCards.size()");
		Assertions.assertEquals(2, size);
		Number amount = context.read("$.cashCards[0].amount");
		Assertions.assertEquals(10.0, amount);
	}

}
