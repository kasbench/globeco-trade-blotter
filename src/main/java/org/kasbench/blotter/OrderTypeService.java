package org.kasbench.blotter;

import java.util.List;
import java.util.Optional;

public interface OrderTypeService {
    List<OrderType> findAll();
    Optional<OrderType> findById(Integer id);
    OrderType save(OrderType orderType);
    OrderType update(Integer id, OrderType orderType);
    void delete(Integer id, Integer version);
} 