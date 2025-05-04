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
    private final BlockRepository blockRepository;
    private final TradeTypeRepository tradeTypeRepository;

    @Autowired
    public TradeController(TradeService service, BlockRepository blockRepository, TradeTypeRepository tradeTypeRepository) {
        this.service = service;
        this.blockRepository = blockRepository;
        this.tradeTypeRepository = tradeTypeRepository;
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
    public Trade createTrade(@RequestBody TradeRequestDTO dto) {
        Trade trade = new Trade();
        trade.setBlock(blockRepository.findById(dto.getBlockId())
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Block not found with id: " + dto.getBlockId())));
        trade.setQuantity(dto.getQuantity());
        trade.setTradeType(tradeTypeRepository.findById(dto.getTradeTypeId())
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("TradeType not found with id: " + dto.getTradeTypeId())));
        trade.setFilledQuantity(dto.getFilledQuantity() != null ? dto.getFilledQuantity() : BigDecimal.ZERO);
        trade.setVersion(dto.getVersion());
        return service.save(trade);
    }

    @PutMapping("/{tradeId}")
    public Trade updateTrade(
            @PathVariable Integer tradeId,
            @RequestBody TradeRequestDTO dto) {
        Trade existing = service.findById(tradeId)
            .orElseThrow(() -> new EntityNotFoundException("Trade not found with id: " + tradeId));
        existing.setBlock(blockRepository.findById(dto.getBlockId())
            .orElseThrow(() -> new EntityNotFoundException("Block not found with id: " + dto.getBlockId())));
        existing.setQuantity(dto.getQuantity());
        existing.setTradeType(tradeTypeRepository.findById(dto.getTradeTypeId())
            .orElseThrow(() -> new EntityNotFoundException("TradeType not found with id: " + dto.getTradeTypeId())));
        existing.setFilledQuantity(dto.getFilledQuantity() != null ? dto.getFilledQuantity() : BigDecimal.ZERO);
        existing.setVersion(dto.getVersion());
        return service.update(tradeId, existing);
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