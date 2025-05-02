package org.kasbench.blotter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderStatusServiceImpl implements OrderStatusService {
    private final OrderStatusRepository repository;

    @Autowired
    public OrderStatusServiceImpl(OrderStatusRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderStatus> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderStatus> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public OrderStatus save(OrderStatus orderStatus) {
        return repository.save(orderStatus);
    }

    @Override
    public OrderStatus update(Integer id, OrderStatus orderStatus) {
        OrderStatus existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order Status not found with id: " + id));
        existing.setAbbreviation(orderStatus.getAbbreviation());
        existing.setDescription(orderStatus.getDescription());
        // version is handled by @Version
        return repository.save(existing);
    }

    @Override
    public void delete(Integer id, Integer version) {
        OrderStatus existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order Status not found with id: " + id));
        if (!version.equals(existing.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(OrderStatus.class, id);
        }
        repository.delete(existing);
    }
} 