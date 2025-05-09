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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService service;
    @Mock
    private SecurityRepository securityRepository;
    @Mock
    private BlotterRepository blotterRepository;
    @Mock
    private OrderTypeRepository orderTypeRepository;
    @Mock
    private OrderStatusRepository orderStatusRepository;

    private OrderController controller;
    private Order sampleOrder;
    private Security sampleSecurity;
    private Blotter sampleBlotter;
    private OrderType sampleOrderType;
    private OrderStatus sampleOrderStatus;
    private SecurityType sampleSecurityType;

    @BeforeEach
    void setUp() {
        controller = new OrderController(
            service, securityRepository, blotterRepository, orderTypeRepository, orderStatusRepository
        );
        
        sampleSecurityType = new SecurityType(1, "STK", "Stock", 1);
        sampleSecurity = new Security(1, "IBM", "IBM Corporation", sampleSecurityType, 1);
        sampleBlotter = new Blotter(1, "Default", false, sampleSecurityType.getId(), 1);
        sampleOrderType = new OrderType(1, "MKT", "Market Order", 1);
        sampleOrderStatus = new OrderStatus(1, "new", "New Order", 1);
        
        sampleOrder = new Order(1, sampleSecurity, sampleBlotter, new BigDecimal("100.00"),
                OffsetDateTime.now(), sampleOrderType, sampleOrderStatus, 1);
    }

    @Test
    void getAllOrders_ReturnsAll() {
        when(service.findAll()).thenReturn(List.of(sampleOrder));
        List<Order> result = controller.getAllOrders();
        assertEquals(1, result.size());
        assertEquals(sampleOrder, result.get(0));
        verify(service).findAll();
    }

    @Test
    void getOrderById_WhenExists_ReturnsOrder() {
        when(service.findById(1)).thenReturn(Optional.of(sampleOrder));
        ResponseEntity<Order> response = controller.getOrderById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleOrder, response.getBody());
        verify(service).findById(1);
    }

    @Test
    void getOrderById_WhenNotExists_ReturnsNotFound() {
        when(service.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<Order> response = controller.getOrderById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service).findById(1);
    }

    @Test
    void createOrder_CreatesAndReturnsOrder() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setSecurityId(1);
        dto.setBlotterId(1);
        dto.setQuantity(new BigDecimal("100.00"));
        dto.setOrderTypeId(1);
        dto.setOrderStatusId(1);
        dto.setVersion(1);

        when(securityRepository.findById(1)).thenReturn(Optional.of(sampleSecurity));
        when(blotterRepository.findById(1)).thenReturn(Optional.of(sampleBlotter));
        when(orderTypeRepository.findById(1)).thenReturn(Optional.of(sampleOrderType));
        when(orderStatusRepository.findById(1)).thenReturn(Optional.of(sampleOrderStatus));
        when(service.save(any(Order.class))).thenReturn(sampleOrder);

        Order result = controller.createOrder(dto);

        assertEquals(sampleOrder, result);
        verify(securityRepository).findById(1);
        verify(blotterRepository).findById(1);
        verify(orderTypeRepository).findById(1);
        verify(orderStatusRepository).findById(1);
        verify(service).save(any(Order.class));
    }

    @Test
    void updateOrder_WhenExists_UpdatesAndReturnsOrder() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setSecurityId(1);
        dto.setBlotterId(1);
        dto.setQuantity(new BigDecimal("100.00"));
        dto.setOrderTypeId(1);
        dto.setOrderStatusId(1);
        dto.setVersion(1);

        when(service.findById(1)).thenReturn(Optional.of(sampleOrder));
        when(securityRepository.findById(1)).thenReturn(Optional.of(sampleSecurity));
        when(blotterRepository.findById(1)).thenReturn(Optional.of(sampleBlotter));
        when(orderTypeRepository.findById(1)).thenReturn(Optional.of(sampleOrderType));
        when(orderStatusRepository.findById(1)).thenReturn(Optional.of(sampleOrderStatus));
        when(service.update(eq(1), any(Order.class))).thenReturn(sampleOrder);

        Order result = controller.updateOrder(1, dto);

        assertEquals(sampleOrder, result);
        verify(service).findById(1);
        verify(securityRepository).findById(1);
        verify(blotterRepository).findById(1);
        verify(orderTypeRepository).findById(1);
        verify(orderStatusRepository).findById(1);
        verify(service).update(eq(1), any(Order.class));
    }

    @Test
    void updateOrder_WhenNotExists_ThrowsException() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setSecurityId(1);
        dto.setBlotterId(1);
        dto.setQuantity(new BigDecimal("100.00"));
        dto.setOrderTypeId(1);
        dto.setOrderStatusId(1);
        dto.setVersion(1);

        when(service.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> controller.updateOrder(1, dto));
        verify(service).findById(1);
    }

    @Test
    void deleteOrder_WhenExistsAndNewStatus_DeletesOrder() {
        doNothing().when(service).delete(1, 1);
        controller.deleteOrder(1, 1);
        verify(service).delete(1, 1);
    }

    @Test
    void deleteOrder_WhenNotNewStatus_ThrowsException() {
        doThrow(new IllegalStateException("Not in new status"))
            .when(service).delete(1, 1);
        assertThrows(IllegalStateException.class,
                () -> controller.deleteOrder(1, 1));
        verify(service).delete(1, 1);
    }

    @Test
    void updateBlotter_Success() {
        when(service.updateBlotter(1, 2, 1)).thenReturn(sampleOrder);
        Order result = controller.updateBlotter(1, 2, 1);
        assertEquals(sampleOrder, result);
        verify(service).updateBlotter(1, 2, 1);
    }

    @Test
    void updateStatus_Success() {
        when(service.updateStatus(1, 2, 1)).thenReturn(sampleOrder);
        Order result = controller.updateStatus(1, 2, 1);
        assertEquals(sampleOrder, result);
        verify(service).updateStatus(1, 2, 1);
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
                new ObjectOptimisticLockingFailureException(Order.class, 1);
        ResponseEntity<String> response = controller.handleOptimisticLock(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().contains("modified by another user"));
    }

    @Test
    void handleIllegalState_ReturnsBadRequestResponse() {
        IllegalStateException ex = new IllegalStateException("Invalid state");
        ResponseEntity<String> response = controller.handleIllegalState(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid state", response.getBody());
    }
} 