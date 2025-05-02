package org.kasbench.blotter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeTypeControllerTest {

    @Mock
    private TradeTypeService service;

    private TradeTypeController controller;
    private TradeType sampleTradeType;

    @BeforeEach
    void setUp() {
        controller = new TradeTypeController(service);
        sampleTradeType = new TradeType(1, "buy", "buy", 1);
    }

    @Test
    void getAllTradeTypes_ReturnsAll() {
        when(service.findAll()).thenReturn(List.of(sampleTradeType));
        List<TradeType> result = controller.getAllTradeTypes();
        assertEquals(1, result.size());
        assertEquals(sampleTradeType, result.get(0));
        verify(service).findAll();
    }

    @Test
    void getTradeTypeById_WhenExists_ReturnsTradeType() {
        when(service.findById(1)).thenReturn(Optional.of(sampleTradeType));
        ResponseEntity<TradeType> response = controller.getTradeTypeById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleTradeType, response.getBody());
        verify(service).findById(1);
    }

    @Test
    void getTradeTypeById_WhenNotExists_ReturnsNotFound() {
        when(service.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<TradeType> response = controller.getTradeTypeById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service).findById(1);
    }

    @Test
    void createTradeType_CreatesAndReturnsTradeType() {
        when(service.save(sampleTradeType)).thenReturn(sampleTradeType);
        TradeType result = controller.createTradeType(sampleTradeType);
        assertEquals(sampleTradeType, result);
        verify(service).save(sampleTradeType);
    }

    @Test
    void updateTradeType_WhenExists_UpdatesAndReturnsTradeType() {
        when(service.update(1, sampleTradeType)).thenReturn(sampleTradeType);
        TradeType result = controller.updateTradeType(1, sampleTradeType);
        assertEquals(sampleTradeType, result);
        verify(service).update(1, sampleTradeType);
    }

    @Test
    void updateTradeType_WhenNotExists_ThrowsException() {
        when(service.update(1, sampleTradeType)).thenThrow(new EntityNotFoundException("Not found"));
        assertThrows(EntityNotFoundException.class,
                () -> controller.updateTradeType(1, sampleTradeType));
        verify(service).update(1, sampleTradeType);
    }

    @Test
    void deleteTradeType_WhenExistsAndVersionMatches_DeletesTradeType() {
        doNothing().when(service).delete(1, 1);
        controller.deleteTradeType(1, 1);
        verify(service).delete(1, 1);
    }

    @Test
    void deleteTradeType_WhenNotExists_ThrowsException() {
        doThrow(new EntityNotFoundException("Not found")).when(service).delete(1, 1);
        assertThrows(EntityNotFoundException.class,
                () -> controller.deleteTradeType(1, 1));
        verify(service).delete(1, 1);
    }

    @Test
    void deleteTradeType_WhenVersionMismatch_ThrowsException() {
        doThrow(new ObjectOptimisticLockingFailureException(TradeType.class, 1)).when(service).delete(1, 2);
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> controller.deleteTradeType(1, 2));
        verify(service).delete(1, 2);
    }

    @Test
    void handleNotFound_ReturnsNotFoundResponse() {
        EntityNotFoundException ex = new EntityNotFoundException("Not found");
        ResponseEntity<String> response = controller.handleNotFound(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody());
    }

    @Test
    void handleOptimisticLock_ReturnsConflictResponse() {
        ObjectOptimisticLockingFailureException ex =
                new ObjectOptimisticLockingFailureException(TradeType.class, 1);
        ResponseEntity<String> response = controller.handleOptimisticLock(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().contains("modified by another user"));
    }
} 