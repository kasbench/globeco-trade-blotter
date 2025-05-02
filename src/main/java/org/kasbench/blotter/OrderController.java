package org.kasbench.blotter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService service;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return service.findAll();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer orderId) {
        return service.findById(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@RequestBody Order order) {
        return service.save(order);
    }

    @PutMapping("/{orderId}")
    public Order updateOrder(
            @PathVariable Integer orderId,
            @RequestBody Order order) {
        return service.update(orderId, order);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(
            @PathVariable Integer orderId,
            @RequestParam Integer versionId) {
        service.delete(orderId, versionId);
    }

    @PostMapping("/{orderId}/blotter/{blotterId}")
    public Order updateBlotter(
            @PathVariable Integer orderId,
            @PathVariable Integer blotterId,
            @RequestParam Integer versionId) {
        return service.updateBlotter(orderId, blotterId, versionId);
    }

    @PostMapping("/{orderId}/status/{statusId}")
    public Order updateStatus(
            @PathVariable Integer orderId,
            @PathVariable Integer statusId,
            @RequestParam Integer versionId) {
        return service.updateStatus(orderId, statusId, versionId);
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

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage());
    }
} 