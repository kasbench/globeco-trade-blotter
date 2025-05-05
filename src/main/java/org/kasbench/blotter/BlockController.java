package org.kasbench.blotter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/block")
public class BlockController {
    private final BlockService service;
    private final SecurityRepository securityRepository;
    private final OrderTypeRepository orderTypeRepository;

    @Autowired
    public BlockController(BlockService service, SecurityRepository securityRepository, OrderTypeRepository orderTypeRepository) {
        this.service = service;
        this.securityRepository = securityRepository;
        this.orderTypeRepository = orderTypeRepository;
    }

    @GetMapping
    public List<Block> getAllBlocks() {
        return service.findAll();
    }

    @GetMapping("/{blockId}")
    public ResponseEntity<Block> getBlockById(@PathVariable Integer blockId) {
        return service.findById(blockId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Block createBlock(@RequestBody BlockRequestDTO dto) {
        Block block = new Block();
        block.setSecurity(securityRepository.findById(dto.getSecurityId())
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Security not found with id: " + dto.getSecurityId())));
        block.setOrderType(orderTypeRepository.findById(dto.getOrderTypeId())
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("OrderType not found with id: " + dto.getOrderTypeId())));
        block.setVersion(dto.getVersion());
        return service.save(block);
    }

    @PutMapping("/{blockId}")
    public Block updateBlock(
            @PathVariable Integer blockId,
            @RequestBody BlockRequestDTO dto) {
        Block existing = service.findById(blockId)
            .orElseThrow(() -> new EntityNotFoundException("Block not found with id: " + blockId));
        existing.setSecurity(securityRepository.findById(dto.getSecurityId())
            .orElseThrow(() -> new EntityNotFoundException("Security not found with id: " + dto.getSecurityId())));
        existing.setOrderType(orderTypeRepository.findById(dto.getOrderTypeId())
            .orElseThrow(() -> new EntityNotFoundException("OrderType not found with id: " + dto.getOrderTypeId())));
        existing.setVersion(dto.getVersion());
        return service.update(blockId, existing);
    }

    @DeleteMapping("/{blockId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBlock(
            @PathVariable Integer blockId,
            @RequestParam Integer versionId) {
        service.delete(blockId, versionId);
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