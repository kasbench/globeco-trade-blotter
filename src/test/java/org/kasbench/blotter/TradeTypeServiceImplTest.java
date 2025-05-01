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
class TradeTypeServiceImplTest {

    @Mock
    private TradeTypeRepository repository;

    private TradeTypeServiceImpl service;
    private TradeType sampleTradeType;

    @BeforeEach
    void setUp() {
        service = new TradeTypeServiceImpl(repository);
        sampleTradeType = new TradeType(1, "buy", "buy", 1);
    }

    @Test
    void findAll_ReturnsAllTradeTypes() {
        when(repository.findAll()).thenReturn(List.of(sampleTradeType));
        List<TradeType> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(sampleTradeType, result.get(0));
        verify(repository).findAll();
    }

    @Test
    void findById_WhenExists_ReturnsTradeType() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleTradeType));
        Optional<TradeType> result = service.findById(1);
        assertTrue(result.isPresent());
        assertEquals(sampleTradeType, result.get());
        verify(repository).findById(1);
    }

    @Test
    void findById_WhenNotExists_ReturnsEmpty() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        Optional<TradeType> result = service.findById(1);
        assertFalse(result.isPresent());
        verify(repository).findById(1);
    }

    @Test
    void save_SavesTradeType() {
        when(repository.save(sampleTradeType)).thenReturn(sampleTradeType);
        TradeType result = service.save(sampleTradeType);
        assertEquals(sampleTradeType, result);
        verify(repository).save(sampleTradeType);
    }

    @Test
    void update_WhenExists_UpdatesTradeType() {
        TradeType updated = new TradeType(1, "sell", "sell", 1);
        when(repository.findById(1)).thenReturn(Optional.of(sampleTradeType));
        when(repository.save(any(TradeType.class))).thenReturn(updated);
        TradeType result = service.update(1, updated);
        assertEquals("sell", result.getAbbreviation());
        assertEquals("sell", result.getDescription());
        verify(repository).findById(1);
        verify(repository).save(any(TradeType.class));
    }

    @Test
    void update_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.update(1, sampleTradeType));
        verify(repository).findById(1);
        verify(repository, never()).save(any(TradeType.class));
    }

    @Test
    void delete_WhenExistsAndVersionMatches_DeletesTradeType() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleTradeType));
        service.delete(1, 1);
        verify(repository).findById(1);
        verify(repository).delete(sampleTradeType);
    }

    @Test
    void delete_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.delete(1, 1));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(TradeType.class));
    }

    @Test
    void delete_WhenVersionMismatch_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleTradeType));
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> service.delete(1, 2));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(TradeType.class));
    }
} 