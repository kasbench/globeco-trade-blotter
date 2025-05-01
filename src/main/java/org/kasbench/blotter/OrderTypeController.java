package org.kasbench.blotter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/order-type")
public class OrderTypeController {
    private final OrderTypeService service;

    @Autowired
    public OrderTypeController(OrderTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrderType> getAllOrderTypes() {
        return service.findAll();
    }

    @GetMapping("/{orderTypeId}")
    public ResponseEntity<OrderType> getOrderTypeById(@PathVariable Integer orderTypeId) {
        return service.findById(orderTypeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderType createOrderType(@RequestBody OrderType orderType) {
        return service.save(orderType);
    }

    @PutMapping("/{orderTypeId}")
    public OrderType updateOrderType(
            @PathVariable Integer orderTypeId,
            @RequestBody OrderType orderType) {
        return service.update(orderTypeId, orderType);
    }

    @DeleteMapping("/{orderTypeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrderType(
            @PathVariable Integer orderTypeId,
            @RequestParam Integer versionId) {
        service.delete(orderTypeId, versionId);
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