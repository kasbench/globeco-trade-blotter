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
public class SecurityTypeServiceImpl implements SecurityTypeService {
    
    private final SecurityTypeRepository repository;

    @Autowired
    public SecurityTypeServiceImpl(SecurityTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SecurityType> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SecurityType> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public SecurityType save(SecurityType securityType) {
        return repository.save(securityType);
    }

    @Override
    public SecurityType update(Integer id, SecurityType securityType) {
        SecurityType existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SecurityType not found with id: " + id));
        
        existing.setAbbreviation(securityType.getAbbreviation());
        existing.setDescription(securityType.getDescription());
        // version is handled by @Version annotation
        
        return repository.save(existing);
    }

    @Override
    public void delete(Integer id, Integer version) {
        SecurityType existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SecurityType not found with id: " + id));
        
        if (!version.equals(existing.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(SecurityType.class, id);
        }
        
        repository.delete(existing);
    }
} 