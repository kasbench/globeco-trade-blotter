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
public class TradeTypeServiceImpl implements TradeTypeService {
    private final TradeTypeRepository repository;

    @Autowired
    public TradeTypeServiceImpl(TradeTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TradeType> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TradeType> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public TradeType save(TradeType tradeType) {
        return repository.save(tradeType);
    }

    @Override
    public TradeType update(Integer id, TradeType tradeType) {
        TradeType existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("TradeType not found with id: " + id));
        existing.setAbbreviation(tradeType.getAbbreviation());
        existing.setDescription(tradeType.getDescription());
        // version is handled by @Version
        return repository.save(existing);
    }

    @Override
    public void delete(Integer id, Integer version) {
        TradeType existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("TradeType not found with id: " + id));
        if (!version.equals(existing.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(TradeType.class, id);
        }
        repository.delete(existing);
    }
} 