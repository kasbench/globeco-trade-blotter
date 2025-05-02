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
public class BlockServiceImpl implements BlockService {
    private final BlockRepository repository;

    @Autowired
    public BlockServiceImpl(BlockRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Block> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Block> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Block save(Block block) {
        return repository.save(block);
    }

    @Override
    public Block update(Integer id, Block block) {
        Block existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Block not found with id: " + id));
        existing.setSecurity(block.getSecurity());
        existing.setOrderType(block.getOrderType());
        // version is handled by @Version
        return repository.save(existing);
    }

    @Override
    public void delete(Integer id, Integer version) {
        Block existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Block not found with id: " + id));
        if (!version.equals(existing.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(Block.class, id);
        }
        repository.delete(existing);
    }
} 