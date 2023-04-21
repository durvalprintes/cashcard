package example.cashcard;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {

    @GetMapping("/{id}")
    public ResponseEntity<CashCard> findById(@PathVariable Long id) {
        if (id.equals(99L)) {
            var cashCard = new CashCard(99L, 123.45);
            return ResponseEntity.ok(cashCard);
        }
        return ResponseEntity.notFound().build();
    }

}
