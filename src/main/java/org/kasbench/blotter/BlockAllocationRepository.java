package org.kasbench.blotter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BlockAllocationRepository extends JpaRepository<BlockAllocation, Integer> {
    List<BlockAllocation> findByBlock_Id(Integer blockId);
    List<BlockAllocation> findByOrder_Id(Integer orderId);
} 