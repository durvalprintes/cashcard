package example.cashcard;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

@JsonTest
public class CashCardJsonTests {

    @Autowired
    private JacksonTester<CashCard> tester;

    @Test
    @DisplayName("Teste de serialização do json")
    public void cashCardSerializationTest() throws IOException {
        CashCard cashCard = new CashCard(99L, 123.45);
        var jsonContent = tester.write(cashCard);

        Assertions.assertThat(jsonContent).isStrictlyEqualToJson("expected.json");
        Assertions.assertThat(jsonContent).hasJsonPathNumberValue("@.id");
        Assertions.assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                .extracting(Number::longValue).isEqualTo(cashCard.id());
        Assertions.assertThat(jsonContent).hasJsonPathNumberValue("@.amount");
        Assertions.assertThat(jsonContent).extractingJsonPathNumberValue("@.amount")
                .isEqualTo(cashCard.amount());
    }

    @Test
    @DisplayName("Teste de deserialização do json")
    public void cashCardDeserializationTest() throws IOException {
        String json = """
                {
                    "id": 99,
                    "amount": 123.45
                }
                """;
        CashCard cashCard = new CashCard(99L, 123.45);

        Assertions.assertThat(tester.parse(json).getObject()).isEqualTo(cashCard);
        Assertions.assertThat(tester.parseObject(json).id()).isEqualTo(cashCard.id());
        Assertions.assertThat(tester.parseObject(json).amount()).isEqualTo(cashCard.amount());
    }
}
