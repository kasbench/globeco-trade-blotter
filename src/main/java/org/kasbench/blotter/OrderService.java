package org.kasbench.blotter;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> findAll();
    Optional<Order> findById(Integer id);
    Order save(Order order);
    Order update(Integer id, Order order);
    void delete(Integer id, Integer version);
    Order updateBlotter(Integer orderId, Integer blotterId, Integer version);
    Order updateStatus(Integer orderId, Integer statusId, Integer version);
} 