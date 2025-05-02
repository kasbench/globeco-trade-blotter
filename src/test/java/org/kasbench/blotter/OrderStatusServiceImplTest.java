package org.kasbench.blotter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderStatusServiceImplTest {

    @Mock
    private OrderStatusRepository repository;

    private OrderStatusServiceImpl service;
    private OrderStatus sampleOrderStatus;

    @BeforeEach
    void setUp() {
        service = new OrderStatusServiceImpl(repository);
        sampleOrderStatus = new OrderStatus(1, "new", "Newly created order", 1);
    }

    @Test
    void findAll_ReturnsAllOrderStatuses() {
        when(repository.findAll()).thenReturn(List.of(sampleOrderStatus));
        List<OrderStatus> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(sampleOrderStatus, result.get(0));
        verify(repository).findAll();
    }

    @Test
    void findById_WhenExists_ReturnsOrderStatus() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrderStatus));
        Optional<OrderStatus> result = service.findById(1);
        assertTrue(result.isPresent());
        assertEquals(sampleOrderStatus, result.get());
        verify(repository).findById(1);
    }

    @Test
    void findById_WhenNotExists_ReturnsEmpty() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        Optional<OrderStatus> result = service.findById(1);
        assertFalse(result.isPresent());
        verify(repository).findById(1);
    }

    @Test
    void save_SavesOrderStatus() {
        when(repository.save(sampleOrderStatus)).thenReturn(sampleOrderStatus);
        OrderStatus result = service.save(sampleOrderStatus);
        assertEquals(sampleOrderStatus, result);
        verify(repository).save(sampleOrderStatus);
    }

    @Test
    void update_WhenExists_UpdatesOrderStatus() {
        OrderStatus updated = new OrderStatus(1, "open", "Order is pending", 1);
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrderStatus));
        when(repository.save(any(OrderStatus.class))).thenReturn(updated);
        OrderStatus result = service.update(1, updated);
        assertEquals("open", result.getAbbreviation());
        assertEquals("Order is pending", result.getDescription());
        verify(repository).findById(1);
        verify(repository).save(any(OrderStatus.class));
    }

    @Test
    void update_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.update(1, sampleOrderStatus));
        verify(repository).findById(1);
        verify(repository, never()).save(any(OrderStatus.class));
    }

    @Test
    void delete_WhenExistsAndVersionMatches_DeletesOrderStatus() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrderStatus));
        service.delete(1, 1);
        verify(repository).findById(1);
        verify(repository).delete(sampleOrderStatus);
    }

    @Test
    void delete_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.delete(1, 1));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(OrderStatus.class));
    }

    @Test
    void delete_WhenVersionMismatch_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrderStatus));
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> service.delete(1, 2));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(OrderStatus.class));
    }
} 