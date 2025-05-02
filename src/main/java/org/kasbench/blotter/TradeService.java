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
    /**
     * Pro rata allocation of trade.filledQuantity to block allocations of the referenced block.
     * The sum of allocated filledQuantity must exactly equal trade.filledQuantity.
     * If off due to rounding, adjust one allocation at random.
     * @param tradeId Trade ID
     * @param versionId Version for optimistic locking
     * @return The updated Trade
     */
    Trade allocateProRata(Integer tradeId, Integer versionId);
} 