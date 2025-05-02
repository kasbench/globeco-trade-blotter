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
class OrderTypeControllerTest {

    @Mock
    private OrderTypeService service;

    private OrderTypeController controller;
    private OrderType sampleOrderType;

    @BeforeEach
    void setUp() {
        controller = new OrderTypeController(service);
        sampleOrderType = new OrderType(1, "MKT", "Market Order", 1);
    }

    @Test
    void getAllOrderTypes_ReturnsAll() {
        when(service.findAll()).thenReturn(List.of(sampleOrderType));
        List<OrderType> result = controller.getAllOrderTypes();
        assertEquals(1, result.size());
        assertEquals(sampleOrderType, result.get(0));
        verify(service).findAll();
    }

    @Test
    void getOrderTypeById_WhenExists_ReturnsOrderType() {
        when(service.findById(1)).thenReturn(Optional.of(sampleOrderType));
        ResponseEntity<OrderType> response = controller.getOrderTypeById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleOrderType, response.getBody());
        verify(service).findById(1);
    }

    @Test
    void getOrderTypeById_WhenNotExists_ReturnsNotFound() {
        when(service.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<OrderType> response = controller.getOrderTypeById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service).findById(1);
    }

    @Test
    void createOrderType_CreatesAndReturnsOrderType() {
        when(service.save(sampleOrderType)).thenReturn(sampleOrderType);
        OrderType result = controller.createOrderType(sampleOrderType);
        assertEquals(sampleOrderType, result);
        verify(service).save(sampleOrderType);
    }

    @Test
    void updateOrderType_WhenExists_UpdatesAndReturnsOrderType() {
        when(service.update(1, sampleOrderType)).thenReturn(sampleOrderType);
        OrderType result = controller.updateOrderType(1, sampleOrderType);
        assertEquals(sampleOrderType, result);
        verify(service).update(1, sampleOrderType);
    }

    @Test
    void updateOrderType_WhenNotExists_ThrowsException() {
        when(service.update(1, sampleOrderType)).thenThrow(new EntityNotFoundException("Not found"));
        assertThrows(EntityNotFoundException.class,
                () -> controller.updateOrderType(1, sampleOrderType));
        verify(service).update(1, sampleOrderType);
    }

    @Test
    void deleteOrderType_WhenExistsAndVersionMatches_DeletesOrderType() {
        doNothing().when(service).delete(1, 1);
        controller.deleteOrderType(1, 1);
        verify(service).delete(1, 1);
    }

    @Test
    void deleteOrderType_WhenNotExists_ThrowsException() {
        doThrow(new EntityNotFoundException("Not found")).when(service).delete(1, 1);
        assertThrows(EntityNotFoundException.class,
                () -> controller.deleteOrderType(1, 1));
        verify(service).delete(1, 1);
    }

    @Test
    void deleteOrderType_WhenVersionMismatch_ThrowsException() {
        doThrow(new ObjectOptimisticLockingFailureException(OrderType.class, 1)).when(service).delete(1, 2);
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> controller.deleteOrderType(1, 2));
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
                new ObjectOptimisticLockingFailureException(OrderType.class, 1);
        ResponseEntity<String> response = controller.handleOptimisticLock(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().contains("modified by another user"));
    }
} 