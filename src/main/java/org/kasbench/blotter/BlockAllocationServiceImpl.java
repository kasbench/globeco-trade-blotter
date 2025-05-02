package org.kasbench.blotter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BlockAllocationServiceImpl implements BlockAllocationService {
    private final BlockAllocationRepository repository;

    @Autowired
    public BlockAllocationServiceImpl(BlockAllocationRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlockAllocation> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BlockAllocation> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlockAllocation> findByBlockId(Integer blockId) {
        return repository.findByBlock_Id(blockId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlockAllocation> findByOrderId(Integer orderId) {
        return repository.findByOrder_Id(orderId);
    }

    @Override
    public BlockAllocation save(BlockAllocation allocation) {
        validateAllocation(allocation);
        return repository.save(allocation);
    }

    @Override
    public BlockAllocation update(Integer id, BlockAllocation allocation) {
        BlockAllocation existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BlockAllocation not found with id: " + id));
        if (!allocation.getVersion().equals(existing.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(BlockAllocation.class, id);
        }
        existing.setOrder(allocation.getOrder());
        existing.setBlock(allocation.getBlock());
        existing.setQuantity(allocation.getQuantity());
        existing.setFilledQuantity(allocation.getFilledQuantity());
        validateAllocation(existing);
        return repository.save(existing);
    }

    @Override
    public void delete(Integer id, Integer version) {
        BlockAllocation existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BlockAllocation not found with id: " + id));
        if (!version.equals(existing.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(BlockAllocation.class, id);
        }
        repository.delete(existing);
    }

    @Override
    public BlockAllocation fillQuantity(Integer id, BigDecimal quantityFilled, Integer version) {
        BlockAllocation allocation = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BlockAllocation not found with id: " + id));
        if (!version.equals(allocation.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(BlockAllocation.class, id);
        }
        BigDecimal newFilled = allocation.getFilledQuantity().add(quantityFilled);
        if (newFilled.compareTo(BigDecimal.ZERO) < 0 || newFilled.compareTo(allocation.getQuantity()) > 0) {
            throw new IllegalArgumentException("Filled quantity must be >= 0 and <= quantity");
        }
        allocation.setFilledQuantity(newFilled);
        return repository.save(allocation);
    }

    private void validateAllocation(BlockAllocation allocation) {
        if (allocation.getQuantity() == null || allocation.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (allocation.getFilledQuantity() == null || allocation.getFilledQuantity().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Filled quantity must be >= 0");
        }
        if (allocation.getFilledQuantity().compareTo(allocation.getQuantity()) > 0) {
            throw new IllegalArgumentException("Filled quantity must be <= quantity");
        }
    }
} 