package org.kasbench.blotter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/trade")
public class TradeController {
    private final TradeService service;

    @Autowired
    public TradeController(TradeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Trade> getAllTrades() {
        return service.findAll();
    }

    @GetMapping("/{tradeId}")
    public ResponseEntity<Trade> getTradeById(@PathVariable Integer tradeId) {
        return service.findById(tradeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/block/{blockId}")
    public List<Trade> getByBlockId(@PathVariable Integer blockId) {
        return service.findByBlockId(blockId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Trade createTrade(@RequestBody Trade trade) {
        return service.save(trade);
    }

    @PutMapping("/{tradeId}")
    public Trade updateTrade(
            @PathVariable Integer tradeId,
            @RequestBody Trade trade) {
        return service.update(tradeId, trade);
    }

    @DeleteMapping("/{tradeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrade(
            @PathVariable Integer tradeId,
            @RequestParam Integer versionId) {
        service.delete(tradeId, versionId);
    }

    @PostMapping("/{tradeId}/fill/{quantityFilled}")
    public Trade fillQuantity(
            @PathVariable Integer tradeId,
            @PathVariable BigDecimal quantityFilled,
            @RequestParam Integer versionId) {
        return service.fillQuantity(tradeId, quantityFilled, versionId);
    }

    @PostMapping("/allocateProRata")
    public Trade allocateProRata(@RequestParam Integer tradeId, @RequestParam Integer versionId) {
        return service.allocateProRata(tradeId, versionId);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleOptimisticLock(ObjectOptimisticLockingFailureException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("The resource was modified by another user. Please try again.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
} 