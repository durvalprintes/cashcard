package example.cashcard;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CashcardApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	@DisplayName("Testa busca de um recurso CashCard")
	void shouldReturnACashCardWhenDataIsSaved() {
		ResponseEntity<String> response = restTemplate.getForEntity("/cashcards/99", String.class);

		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

		DocumentContext context = JsonPath.parse(response.getBody());

		Number id = context.read("$.id");
		Assertions.assertNotNull(id);
		Assertions.assertEquals(99L, id.longValue());

		Number amount = context.read("$.amount");
		Assertions.assertNotNull(amount);
		Assertions.assertEquals(123.45, amount.doubleValue());
	}

	@Test
	@DisplayName("Testa falha na busca de um recurso CashCard")
	void shouldNotReturnACashCardWithAnUnknownId() {
		ResponseEntity<String> response = restTemplate.getForEntity("/cashcards/1000", String.class);

		Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		Assertions.assertTrue(Optional.ofNullable(response.getBody()).orElse("").isBlank());
	}

}
