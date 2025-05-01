package org.kasbench.blotter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeTypeRepository extends JpaRepository<TradeType, Integer> {
} 