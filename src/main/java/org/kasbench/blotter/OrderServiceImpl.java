package org.kasbench.blotter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;
    private final BlotterRepository blotterRepository;
    private final OrderStatusRepository orderStatusRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository repository, 
                          BlotterRepository blotterRepository,
                          OrderStatusRepository orderStatusRepository) {
        this.repository = repository;
        this.blotterRepository = blotterRepository;
        this.orderStatusRepository = orderStatusRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Order save(Order order) {
        // Set order timestamp to current time
        order.setOrderTimestamp(OffsetDateTime.now());

        // Set default order status if not provided
        if (order.getOrderStatus() == null) {
            orderStatusRepository.findById(1)
                .ifPresent(order::setOrderStatus);
        }

        // Apply blotter assignment rule
        if (order.getBlotter() == null && order.getSecurity() != null) {
            blotterRepository.findAll().stream()
                .filter(blotter -> blotter.getAutoPopulate() && 
                        blotter.getSecurityTypeId() != null &&
                        blotter.getSecurityTypeId().equals(order.getSecurity().getSecurityType().getId()))
                .findFirst()
                .ifPresent(order::setBlotter);
        }

        return repository.save(order);
    }

    @Override
    public Order update(Integer id, Order order) {
        Order existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        
        existing.setSecurity(order.getSecurity());
        existing.setBlotter(order.getBlotter());
        existing.setQuantity(order.getQuantity());
        existing.setOrderType(order.getOrderType());
        existing.setOrderStatus(order.getOrderStatus());
        // orderTimestamp is not updated
        // version is handled by @Version
        
        return repository.save(existing);
    }

    @Override
    public void delete(Integer id, Integer version) {
        Order existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        
        if (!version.equals(existing.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(Order.class, id);
        }

        // Only allow delete if order status is 1 (new)
        if (existing.getOrderStatus() == null || existing.getOrderStatus().getId() != 1) {
            throw new IllegalStateException("Order can only be deleted when in 'new' status");
        }

        repository.delete(existing);
    }

    @Override
    public Order updateBlotter(Integer orderId, Integer blotterId, Integer version) {
        Order order = repository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        if (!version.equals(order.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(Order.class, orderId);
        }

        Blotter blotter = blotterRepository.findById(blotterId)
            .orElseThrow(() -> new EntityNotFoundException("Blotter not found with id: " + blotterId));

        order.setBlotter(blotter);
        return repository.save(order);
    }

    @Override
    public Order updateStatus(Integer orderId, Integer statusId, Integer version) {
        Order order = repository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

        if (!version.equals(order.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(Order.class, orderId);
        }

        OrderStatus status = orderStatusRepository.findById(statusId)
            .orElseThrow(() -> new EntityNotFoundException("Order Status not found with id: " + statusId));

        order.setOrderStatus(status);
        return repository.save(order);
    }
} 