package org.kasbench.blotter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/tradeType")
public class TradeTypeController {
    private final TradeTypeService service;

    @Autowired
    public TradeTypeController(TradeTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<TradeType> getAllTradeTypes() {
        return service.findAll();
    }

    @GetMapping("/{tradeTypeId}")
    public ResponseEntity<TradeType> getTradeTypeById(@PathVariable Integer tradeTypeId) {
        return service.findById(tradeTypeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TradeType createTradeType(@RequestBody TradeType tradeType) {
        return service.save(tradeType);
    }

    @PutMapping("/{tradeTypeId}")
    public TradeType updateTradeType(
            @PathVariable Integer tradeTypeId,
            @RequestBody TradeType tradeType) {
        return service.update(tradeTypeId, tradeType);
    }

    @DeleteMapping("/{tradeTypeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTradeType(
            @PathVariable Integer tradeTypeId,
            @RequestParam Integer versionId) {
        service.delete(tradeTypeId, versionId);
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
} 