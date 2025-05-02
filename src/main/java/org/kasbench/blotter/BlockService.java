package org.kasbench.blotter;

import java.util.List;
import java.util.Optional;

public interface BlockService {
    List<Block> findAll();
    Optional<Block> findById(Integer id);
    Block save(Block block);
    Block update(Integer id, Block block);
    void delete(Integer id, Integer version);
} 