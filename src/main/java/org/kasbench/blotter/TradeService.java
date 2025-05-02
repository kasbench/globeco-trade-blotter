package org.kasbench.blotter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TradeService {
    List<Trade> findAll();
    Optional<Trade> findById(Integer id);
    List<Trade> findByBlockId(Integer blockId);
    Trade save(Trade trade);
    Trade update(Integer id, Trade trade);
    void delete(Integer id, Integer version);
    Trade fillQuantity(Integer id, BigDecimal quantityFilled, Integer version);
} 