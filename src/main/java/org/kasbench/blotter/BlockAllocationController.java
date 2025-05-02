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
@RequestMapping("/blockAllocation")
public class BlockAllocationController {
    private final BlockAllocationService service;

    @Autowired
    public BlockAllocationController(BlockAllocationService service) {
        this.service = service;
    }

    @GetMapping
    public List<BlockAllocation> getAllBlockAllocations() {
        return service.findAll();
    }

    @GetMapping("/{blockAllocationId}")
    public ResponseEntity<BlockAllocation> getBlockAllocationById(@PathVariable Integer blockAllocationId) {
        return service.findById(blockAllocationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/block/{blockId}")
    public List<BlockAllocation> getByBlockId(@PathVariable Integer blockId) {
        return service.findByBlockId(blockId);
    }

    @GetMapping("/order/{orderId}")
    public List<BlockAllocation> getByOrderId(@PathVariable Integer orderId) {
        return service.findByOrderId(orderId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BlockAllocation createBlockAllocation(@RequestBody BlockAllocation allocation) {
        return service.save(allocation);
    }

    @PutMapping("/{blockAllocationId}")
    public BlockAllocation updateBlockAllocation(
            @PathVariable Integer blockAllocationId,
            @RequestBody BlockAllocation allocation) {
        return service.update(blockAllocationId, allocation);
    }

    @DeleteMapping("/{blockAllocationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBlockAllocation(
            @PathVariable Integer blockAllocationId,
            @RequestParam Integer versionId) {
        service.delete(blockAllocationId, versionId);
    }

    @PostMapping("/{blockAllocationId}/fill/{quantityFilled}")
    public BlockAllocation fillQuantity(
            @PathVariable Integer blockAllocationId,
            @PathVariable BigDecimal quantityFilled,
            @RequestParam Integer versionId) {
        return service.fillQuantity(blockAllocationId, quantityFilled, versionId);
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