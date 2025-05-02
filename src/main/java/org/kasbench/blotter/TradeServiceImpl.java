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
public class TradeServiceImpl implements TradeService {
    private final TradeRepository repository;

    @Autowired
    public TradeServiceImpl(TradeRepository repository) {
        this.repository = repository;
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