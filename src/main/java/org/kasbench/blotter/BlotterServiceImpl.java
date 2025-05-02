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
public class BlotterServiceImpl implements BlotterService {
    private final BlotterRepository repository;

    @Autowired
    public BlotterServiceImpl(BlotterRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Blotter> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Blotter> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Blotter save(Blotter blotter) {
        return repository.save(blotter);
    }

    @Override
    public Blotter update(Integer id, Blotter blotter) {
        Blotter existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Blotter not found with id: " + id));
        existing.setName(blotter.getName());
        existing.setAutoPopulate(blotter.getAutoPopulate());
        existing.setSecurityTypeId(blotter.getSecurityTypeId());
        // version is handled by @Version
        return repository.save(existing);
    }

    @Override
    public void delete(Integer id, Integer version) {
        Blotter existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Blotter not found with id: " + id));
        if (!version.equals(existing.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(Blotter.class, id);
        }
        repository.delete(existing);
    }
} 