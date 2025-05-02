package org.kasbench.blotter;

import java.util.List;
import java.util.Optional;

public interface OrderStatusService {
    List<OrderStatus> findAll();
    Optional<OrderStatus> findById(Integer id);
    OrderStatus save(OrderStatus orderStatus);
    OrderStatus update(Integer id, OrderStatus orderStatus);
    void delete(Integer id, Integer version);
} 