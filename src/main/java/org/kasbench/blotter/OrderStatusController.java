package org.kasbench.blotter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/orderStatus")
public class OrderStatusController {
    private final OrderStatusService service;

    @Autowired
    public OrderStatusController(OrderStatusService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrderStatus> getAllOrderStatuses() {
        return service.findAll();
    }

    @GetMapping("/{orderStatusId}")
    public ResponseEntity<OrderStatus> getOrderStatusById(@PathVariable Integer orderStatusId) {
        return service.findById(orderStatusId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderStatus createOrderStatus(@RequestBody OrderStatus orderStatus) {
        return service.save(orderStatus);
    }

    @PutMapping("/{orderStatusId}")
    public OrderStatus updateOrderStatus(
            @PathVariable Integer orderStatusId,
            @RequestBody OrderStatus orderStatus) {
        return service.update(orderStatusId, orderStatus);
    }

    @DeleteMapping("/{orderStatusId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrderStatus(
            @PathVariable Integer orderStatusId,
            @RequestParam Integer versionId) {
        service.delete(orderStatusId, versionId);
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