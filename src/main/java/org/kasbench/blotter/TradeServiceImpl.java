package org.kasbench.blotter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class TradeServiceImpl implements TradeService {
    private final TradeRepository repository;
    private final BlockAllocationService blockAllocationService;

    @Autowired
    public TradeServiceImpl(TradeRepository repository, BlockAllocationService blockAllocationService) {
        this.repository = repository;
        this.blockAllocationService = blockAllocationService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trade> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trade> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trade> findByBlockId(Integer blockId) {
        return repository.findByBlock_Id(blockId);
    }

    @Override
    public Trade save(Trade trade) {
        validateTrade(trade);
        return repository.save(trade);
    }

    @Override
    public Trade update(Integer id, Trade trade) {
        Trade existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Trade not found with id: " + id));
        if (!trade.getVersion().equals(existing.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(Trade.class, id);
        }
        existing.setBlock(trade.getBlock());
        existing.setQuantity(trade.getQuantity());
        existing.setTradeType(trade.getTradeType());
        existing.setFilledQuantity(trade.getFilledQuantity());
        validateTrade(existing);
        return repository.save(existing);
    }

    @Override
    public void delete(Integer id, Integer version) {
        Trade existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Trade not found with id: " + id));
        if (!version.equals(existing.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(Trade.class, id);
        }
        repository.delete(existing);
    }

    @Override
    public Trade fillQuantity(Integer id, BigDecimal quantityFilled, Integer version) {
        Trade trade = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Trade not found with id: " + id));
        if (!version.equals(trade.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(Trade.class, id);
        }
        BigDecimal newFilled = trade.getFilledQuantity().add(quantityFilled);
        if (newFilled.compareTo(BigDecimal.ZERO) < 0 || newFilled.compareTo(trade.getQuantity()) > 0) {
            throw new IllegalArgumentException("Filled quantity must be >= 0 and <= quantity");
        }
        trade.setFilledQuantity(newFilled);
        return repository.save(trade);
    }

    @Override
    public Trade allocateProRata(Integer tradeId, Integer versionId) {
        Trade trade = repository.findById(tradeId)
            .orElseThrow(() -> new EntityNotFoundException("Trade not found with id: " + tradeId));
        if (!versionId.equals(trade.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(Trade.class, tradeId);
        }
        List<BlockAllocation> allocations = blockAllocationService.findByBlockId(trade.getBlock().getId());
        if (allocations.isEmpty()) {
            throw new IllegalArgumentException("No block allocations found for block: " + trade.getBlock().getId());
        }
        BigDecimal totalQuantity = allocations.stream()
            .map(BlockAllocation::getQuantity)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalQuantity.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Total allocation quantity is zero");
        }
        BigDecimal tradeFilled = trade.getFilledQuantity();
        // Calculate pro rata allocations
        List<BigDecimal> rawAllocations = new java.util.ArrayList<>();
        List<BigDecimal> roundedAllocations = new java.util.ArrayList<>();
        BigDecimal sumRounded = BigDecimal.ZERO;
        for (BlockAllocation alloc : allocations) {
            BigDecimal ratio = alloc.getQuantity().divide(totalQuantity, 16, java.math.RoundingMode.HALF_UP);
            BigDecimal allocFilled = tradeFilled.multiply(ratio);
            BigDecimal rounded = allocFilled.setScale(8, java.math.RoundingMode.HALF_UP);
            rawAllocations.add(allocFilled);
            roundedAllocations.add(rounded);
            sumRounded = sumRounded.add(rounded);
        }
        // Adjust for rounding error
        BigDecimal diff = tradeFilled.subtract(sumRounded);
        if (diff.compareTo(BigDecimal.ZERO) != 0) {
            // Pick a random allocation to adjust
            int idx = new Random().nextInt(allocations.size());
            roundedAllocations.set(idx, roundedAllocations.get(idx).add(diff));
            sumRounded = sumRounded.add(diff);
        }
        // Apply allocations
        for (int i = 0; i < allocations.size(); i++) {
            BlockAllocation alloc = allocations.get(i);
            alloc.setFilledQuantity(roundedAllocations.get(i));
            blockAllocationService.update(alloc.getId(), alloc);
        }
        return trade;
    }

    private void validateTrade(Trade trade) {
        if (trade.getQuantity() == null || trade.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (trade.getFilledQuantity() == null || trade.getFilledQuantity().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Filled quantity must be >= 0");
        }
        if (trade.getFilledQuantity().compareTo(trade.getQuantity()) > 0) {
            throw new IllegalArgumentException("Filled quantity must be <= quantity");
        }
    }
} 