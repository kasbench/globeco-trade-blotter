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
class OrderStatusControllerTest {

    @Mock
    private OrderStatusService service;

    private OrderStatusController controller;
    private OrderStatus sampleOrderStatus;

    @BeforeEach
    void setUp() {
        controller = new OrderStatusController(service);
        sampleOrderStatus = new OrderStatus(1, "new", "Newly created order", 1);
    }

    @Test
    void getAllOrderStatuses_ReturnsAll() {
        when(service.findAll()).thenReturn(List.of(sampleOrderStatus));
        List<OrderStatus> result = controller.getAllOrderStatuses();
        assertEquals(1, result.size());
        assertEquals(sampleOrderStatus, result.get(0));
        verify(service).findAll();
    }

    @Test
    void getOrderStatusById_WhenExists_ReturnsOrderStatus() {
        when(service.findById(1)).thenReturn(Optional.of(sampleOrderStatus));
        ResponseEntity<OrderStatus> response = controller.getOrderStatusById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleOrderStatus, response.getBody());
        verify(service).findById(1);
    }

    @Test
    void getOrderStatusById_WhenNotExists_ReturnsNotFound() {
        when(service.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<OrderStatus> response = controller.getOrderStatusById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service).findById(1);
    }

    @Test
    void createOrderStatus_CreatesAndReturnsOrderStatus() {
        when(service.save(sampleOrderStatus)).thenReturn(sampleOrderStatus);
        OrderStatus result = controller.createOrderStatus(sampleOrderStatus);
        assertEquals(sampleOrderStatus, result);
        verify(service).save(sampleOrderStatus);
    }

    @Test
    void updateOrderStatus_WhenExists_UpdatesAndReturnsOrderStatus() {
        when(service.update(1, sampleOrderStatus)).thenReturn(sampleOrderStatus);
        OrderStatus result = controller.updateOrderStatus(1, sampleOrderStatus);
        assertEquals(sampleOrderStatus, result);
        verify(service).update(1, sampleOrderStatus);
    }

    @Test
    void updateOrderStatus_WhenNotExists_ThrowsException() {
        when(service.update(1, sampleOrderStatus)).thenThrow(new EntityNotFoundException("Not found"));
        assertThrows(EntityNotFoundException.class,
                () -> controller.updateOrderStatus(1, sampleOrderStatus));
        verify(service).update(1, sampleOrderStatus);
    }

    @Test
    void deleteOrderStatus_WhenExistsAndVersionMatches_DeletesOrderStatus() {
        doNothing().when(service).delete(1, 1);
        controller.deleteOrderStatus(1, 1);
        verify(service).delete(1, 1);
    }

    @Test
    void deleteOrderStatus_WhenNotExists_ThrowsException() {
        doThrow(new EntityNotFoundException("Not found")).when(service).delete(1, 1);
        assertThrows(EntityNotFoundException.class,
                () -> controller.deleteOrderStatus(1, 1));
        verify(service).delete(1, 1);
    }

    @Test
    void deleteOrderStatus_WhenVersionMismatch_ThrowsException() {
        doThrow(new ObjectOptimisticLockingFailureException(OrderStatus.class, 1)).when(service).delete(1, 2);
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> controller.deleteOrderStatus(1, 2));
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
                new ObjectOptimisticLockingFailureException(OrderStatus.class, 1);
        ResponseEntity<String> response = controller.handleOptimisticLock(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().contains("modified by another user"));
    }
} 