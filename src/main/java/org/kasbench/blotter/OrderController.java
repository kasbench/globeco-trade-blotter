package org.kasbench.blotter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.math.BigDecimal;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService service;
    private final SecurityRepository securityRepository;
    private final BlotterRepository blotterRepository;
    private final OrderTypeRepository orderTypeRepository;
    private final OrderStatusRepository orderStatusRepository;

    @Autowired
    public OrderController(OrderService service, SecurityRepository securityRepository, BlotterRepository blotterRepository, OrderTypeRepository orderTypeRepository, OrderStatusRepository orderStatusRepository) {
        this.service = service;
        this.securityRepository = securityRepository;
        this.blotterRepository = blotterRepository;
        this.orderTypeRepository = orderTypeRepository;
        this.orderStatusRepository = orderStatusRepository;
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
    public Order createOrder(@RequestBody OrderRequestDTO dto) {
        Order order = new Order();
        order.setSecurity(securityRepository.findById(dto.getSecurityId())
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Security not found with id: " + dto.getSecurityId())));
        if (dto.getBlotterId() != null) {
            order.setBlotter(blotterRepository.findById(dto.getBlotterId())
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Blotter not found with id: " + dto.getBlotterId())));
        }
        order.setQuantity(dto.getQuantity());
        order.setOrderType(orderTypeRepository.findById(dto.getOrderTypeId())
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("OrderType not found with id: " + dto.getOrderTypeId())));
        if (dto.getOrderStatusId() != null) {
            order.setOrderStatus(orderStatusRepository.findById(dto.getOrderStatusId())
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("OrderStatus not found with id: " + dto.getOrderStatusId())));
        }
        order.setVersion(dto.getVersion());
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