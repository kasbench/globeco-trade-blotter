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
class OrderTypeServiceImplTest {

    @Mock
    private OrderTypeRepository repository;

    private OrderTypeServiceImpl service;
    private OrderType sampleOrderType;

    @BeforeEach
    void setUp() {
        service = new OrderTypeServiceImpl(repository);
        sampleOrderType = new OrderType(1, "MKT", "Market Order", 1);
    }

    @Test
    void findAll_ReturnsAllOrderTypes() {
        when(repository.findAll()).thenReturn(List.of(sampleOrderType));
        List<OrderType> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(sampleOrderType, result.get(0));
        verify(repository).findAll();
    }

    @Test
    void findById_WhenExists_ReturnsOrderType() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrderType));
        Optional<OrderType> result = service.findById(1);
        assertTrue(result.isPresent());
        assertEquals(sampleOrderType, result.get());
        verify(repository).findById(1);
    }

    @Test
    void findById_WhenNotExists_ReturnsEmpty() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        Optional<OrderType> result = service.findById(1);
        assertFalse(result.isPresent());
        verify(repository).findById(1);
    }

    @Test
    void save_SavesOrderType() {
        when(repository.save(sampleOrderType)).thenReturn(sampleOrderType);
        OrderType result = service.save(sampleOrderType);
        assertEquals(sampleOrderType, result);
        verify(repository).save(sampleOrderType);
    }

    @Test
    void update_WhenExists_UpdatesOrderType() {
        OrderType updated = new OrderType(1, "LMT", "Limit Order", 1);
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrderType));
        when(repository.save(any(OrderType.class))).thenReturn(updated);
        OrderType result = service.update(1, updated);
        assertEquals("LMT", result.getAbbreviation());
        assertEquals("Limit Order", result.getDescription());
        verify(repository).findById(1);
        verify(repository).save(any(OrderType.class));
    }

    @Test
    void update_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.update(1, sampleOrderType));
        verify(repository).findById(1);
        verify(repository, never()).save(any(OrderType.class));
    }

    @Test
    void delete_WhenExistsAndVersionMatches_DeletesOrderType() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrderType));
        service.delete(1, 1);
        verify(repository).findById(1);
        verify(repository).delete(sampleOrderType);
    }

    @Test
    void delete_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.delete(1, 1));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(OrderType.class));
    }

    @Test
    void delete_WhenVersionMismatch_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrderType));
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> service.delete(1, 2));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(OrderType.class));
    }
} 