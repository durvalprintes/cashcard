package example.cashcard;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/cashcards")
@RequiredArgsConstructor
public class CashCardController {

    private final CashCardRepository repository;

    @GetMapping("/{id}")
    public ResponseEntity<CashCard> findById(@PathVariable Long id) {
        Optional<CashCard> cashCard = repository.findById(id);
        return cashCard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CashCard> save(@RequestBody CashCardRequest request) {
        CashCard cashCard = repository.save(new CashCard(null, request.amount()));
        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(cashCard.id())
                                .toUri())
                .build();
    }

    @GetMapping
    public ResponseEntity<CashCardPage> findAll(Pageable pageable) {
        Page<CashCard> cashCards = repository.findAll(pageable);
        return ResponseEntity.ok(new CashCardPage(cashCards.getContent(), cashCards.getTotalElements(), cashCards.getTotalPages()));
    }

}