package org.kasbench.blotter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository repository;
    @Mock
    private BlotterRepository blotterRepository;
    @Mock
    private OrderStatusRepository orderStatusRepository;

    private OrderServiceImpl service;
    private Order sampleOrder;
    private Security sampleSecurity;
    private Blotter sampleBlotter;
    private OrderType sampleOrderType;
    private OrderStatus sampleOrderStatus;
    private SecurityType sampleSecurityType;

    @BeforeEach
    void setUp() {
        service = new OrderServiceImpl(repository, blotterRepository, orderStatusRepository);
        
        sampleSecurityType = new SecurityType(1, "STK", "Stock", 1);
        sampleSecurity = new Security(1, "IBM", "IBM Corporation", sampleSecurityType, 1);
        sampleBlotter = new Blotter(1, "Default", false, null, 1);
        sampleOrderType = new OrderType(1, "MKT", "Market Order", 1);
        sampleOrderStatus = new OrderStatus(1, "new", "New Order", 1);
        
        sampleOrder = new Order(1, sampleSecurity, sampleBlotter, new BigDecimal("100.00"),
                OffsetDateTime.now(), sampleOrderType, sampleOrderStatus, 1);
    }

    @Test
    void findAll_ReturnsAllOrders() {
        when(repository.findAll()).thenReturn(List.of(sampleOrder));
        List<Order> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(sampleOrder, result.get(0));
        verify(repository).findAll();
    }

    @Test
    void findById_WhenExists_ReturnsOrder() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrder));
        Optional<Order> result = service.findById(1);
        assertTrue(result.isPresent());
        assertEquals(sampleOrder, result.get());
        verify(repository).findById(1);
    }

    @Test
    void findById_WhenNotExists_ReturnsEmpty() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        Optional<Order> result = service.findById(1);
        assertFalse(result.isPresent());
        verify(repository).findById(1);
    }

    @Test
    void save_SetsDefaultOrderStatus() {
        Order newOrder = new Order();
        newOrder.setSecurity(sampleSecurity);
        newOrder.setOrderType(sampleOrderType);
        when(orderStatusRepository.findById(1)).thenReturn(Optional.of(sampleOrderStatus));
        when(repository.save(any(Order.class))).thenReturn(sampleOrder);

        Order result = service.save(newOrder);
        assertNotNull(result.getOrderStatus());
        assertEquals(sampleOrderStatus, result.getOrderStatus());
        verify(repository).save(any(Order.class));
    }

    @Test
    void save_AppliesBlotterAssignmentRule() {
        Order newOrder = new Order();
        newOrder.setSecurity(sampleSecurity);
        newOrder.setOrderType(sampleOrderType);
        
        Blotter autoBlotter = new Blotter(2, "Auto", true, sampleSecurityType.getId(), 1);
        when(blotterRepository.findAll()).thenReturn(List.of(autoBlotter));
        when(repository.save(any(Order.class))).thenReturn(sampleOrder);

        Order result = service.save(newOrder);
        verify(repository).save(any(Order.class));
        verify(blotterRepository).findAll();
    }

    @Test
    void update_WhenExists_UpdatesOrder() {
        Order updated = new Order(1, sampleSecurity, sampleBlotter, new BigDecimal("200.00"),
                OffsetDateTime.now(), sampleOrderType, sampleOrderStatus, 1);
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrder));
        when(repository.save(any(Order.class))).thenReturn(updated);

        Order result = service.update(1, updated);
        assertEquals(new BigDecimal("200.00"), result.getQuantity());
        verify(repository).findById(1);
        verify(repository).save(any(Order.class));
    }

    @Test
    void update_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.update(1, sampleOrder));
        verify(repository).findById(1);
        verify(repository, never()).save(any(Order.class));
    }

    @Test
    void delete_WhenExistsAndNewStatus_DeletesOrder() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrder));
        service.delete(1, 1);
        verify(repository).findById(1);
        verify(repository).delete(sampleOrder);
    }

    @Test
    void delete_WhenNotNewStatus_ThrowsException() {
        OrderStatus nonNewStatus = new OrderStatus(2, "open", "Open Order", 1);
        sampleOrder.setOrderStatus(nonNewStatus);
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrder));
        
        assertThrows(IllegalStateException.class,
                () -> service.delete(1, 1));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(Order.class));
    }

    @Test
    void updateBlotter_Success() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrder));
        when(blotterRepository.findById(2)).thenReturn(Optional.of(new Blotter(2, "New", false, null, 1)));
        when(repository.save(any(Order.class))).thenReturn(sampleOrder);

        Order result = service.updateBlotter(1, 2, 1);
        assertNotNull(result);
        verify(repository).findById(1);
        verify(blotterRepository).findById(2);
        verify(repository).save(any(Order.class));
    }

    @Test
    void updateStatus_Success() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrder));
        when(orderStatusRepository.findById(2)).thenReturn(Optional.of(new OrderStatus(2, "open", "Open", 1)));
        when(repository.save(any(Order.class))).thenReturn(sampleOrder);

        Order result = service.updateStatus(1, 2, 1);
        assertNotNull(result);
        verify(repository).findById(1);
        verify(orderStatusRepository).findById(2);
        verify(repository).save(any(Order.class));
    }
} 