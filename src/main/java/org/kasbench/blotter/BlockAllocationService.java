package org.kasbench.blotter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BlockAllocationService {
    List<BlockAllocation> findAll();
    Optional<BlockAllocation> findById(Integer id);
    List<BlockAllocation> findByBlockId(Integer blockId);
    List<BlockAllocation> findByOrderId(Integer orderId);
    BlockAllocation save(BlockAllocation allocation);
    BlockAllocation update(Integer id, BlockAllocation allocation);
    void delete(Integer id, Integer version);
    BlockAllocation fillQuantity(Integer id, BigDecimal quantityFilled, Integer version);
} 