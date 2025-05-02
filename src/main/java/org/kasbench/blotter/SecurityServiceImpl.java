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
public class SecurityServiceImpl implements SecurityService {
    private final SecurityRepository repository;

    @Autowired
    public SecurityServiceImpl(SecurityRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Security> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Security> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Security save(Security security) {
        return repository.save(security);
    }

    @Override
    public Security update(Integer id, Security security) {
        Security existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Security not found with id: " + id));
        existing.setTicker(security.getTicker());
        existing.setDescription(security.getDescription());
        existing.setSecurityType(security.getSecurityType());
        // version is handled by @Version
        return repository.save(existing);
    }

    @Override
    public void delete(Integer id, Integer version) {
        Security existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Security not found with id: " + id));
        if (!version.equals(existing.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(Security.class, id);
        }
        repository.delete(existing);
    }
} 