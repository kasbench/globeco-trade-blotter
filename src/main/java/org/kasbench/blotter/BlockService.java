package org.kasbench.blotter;

import java.util.List;
import java.util.Optional;

public interface BlockService {
    List<Block> findAll();
    Optional<Block> findById(Integer id);
    Block save(Block block);
    Block update(Integer id, Block block);
    void delete(Integer id, Integer version);
    /**
     * Creates a block and associated block allocations from a list of orderIds.
     * All orders must have the same securityId and orderTypeId.
     * @param orderIds List of order IDs
     * @return The created Block
     */
    Block createBlockWithAllocations(List<Integer> orderIds);
} 