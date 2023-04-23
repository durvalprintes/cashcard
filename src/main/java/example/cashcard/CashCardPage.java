package example.cashcard;

import java.util.List;

public record CashCardPage(List<CashCard> cashCards, Long elements, int pages) {
}
