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
public class OrderTypeServiceImpl implements OrderTypeService {
    private final OrderTypeRepository repository;

    @Autowired
    public OrderTypeServiceImpl(OrderTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderType> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderType> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public OrderType save(OrderType orderType) {
        return repository.save(orderType);
    }

    @Override
    public OrderType update(Integer id, OrderType orderType) {
        OrderType existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order Type not found with id: " + id));
        existing.setAbbreviation(orderType.getAbbreviation());
        existing.setDescription(orderType.getDescription());
        // version is handled by @Version
        return repository.save(existing);
    }

    @Override
    public void delete(Integer id, Integer version) {
        OrderType existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order Type not found with id: " + id));
        if (!version.equals(existing.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(OrderType.class, id);
        }
        repository.delete(existing);
    }
} 