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
public class DestinationServiceImpl implements DestinationService {
    private final DestinationRepository repository;

    @Autowired
    public DestinationServiceImpl(DestinationRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Destination> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Destination> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Destination save(Destination destination) {
        return repository.save(destination);
    }

    @Override
    public Destination update(Integer id, Destination destination) {
        Destination existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Destination not found with id: " + id));
        existing.setAbbreviation(destination.getAbbreviation());
        existing.setDescription(destination.getDescription());
        // version is handled by @Version
        return repository.save(existing);
    }

    @Override
    public void delete(Integer id, Integer version) {
        Destination existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Destination not found with id: " + id));
        if (!version.equals(existing.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(Destination.class, id);
        }
        repository.delete(existing);
    }
} 