package org.kasbench.blotter;

import java.util.List;
import java.util.Optional;

public interface TradeTypeService {
    List<TradeType> findAll();
    Optional<TradeType> findById(Integer id);
    TradeType save(TradeType tradeType);
    TradeType update(Integer id, TradeType tradeType);
    void delete(Integer id, Integer version);
} 