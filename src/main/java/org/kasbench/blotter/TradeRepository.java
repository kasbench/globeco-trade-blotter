package org.kasbench.blotter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Integer> {
    List<Trade> findByBlock_Id(Integer blockId);
} 