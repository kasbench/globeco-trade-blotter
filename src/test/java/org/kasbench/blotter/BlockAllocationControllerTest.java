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
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlockAllocationControllerTest {
    @Mock
    private BlockAllocationService service;
    private BlockAllocationController controller;
    private BlockAllocation sampleAlloc;
    private Order sampleOrder;
    private Block sampleBlock;

    @BeforeEach
    void setUp() {
        controller = new BlockAllocationController(service);
        sampleOrder = new Order();
        sampleOrder.setId(1);
        sampleBlock = new Block();
        sampleBlock.setId(2);
        sampleAlloc = new BlockAllocation(1, sampleOrder, sampleBlock, new BigDecimal("100.00"), new BigDecimal("50.00"), 1);
    }

    @Test
    void getAllBlockAllocations_ReturnsAll() {
        when(service.findAll()).thenReturn(List.of(sampleAlloc));
        List<BlockAllocation> result = controller.getAllBlockAllocations();
        assertEquals(1, result.size());
        assertEquals(sampleAlloc, result.get(0));
        verify(service).findAll();
    }

    @Test
    void getBlockAllocationById_WhenExists_ReturnsAlloc() {
        when(service.findById(1)).thenReturn(Optional.of(sampleAlloc));
        ResponseEntity<BlockAllocation> response = controller.getBlockAllocationById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleAlloc, response.getBody());
        verify(service).findById(1);
    }

    @Test
    void getBlockAllocationById_WhenNotExists_ReturnsNotFound() {
        when(service.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<BlockAllocation> response = controller.getBlockAllocationById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service).findById(1);
    }

    @Test
    void getByBlockId_ReturnsList() {
        when(service.findByBlockId(2)).thenReturn(List.of(sampleAlloc));
        List<BlockAllocation> result = controller.getByBlockId(2);
        assertEquals(1, result.size());
        assertEquals(sampleAlloc, result.get(0));
        verify(service).findByBlockId(2);
    }

    @Test
    void getByOrderId_ReturnsList() {
        when(service.findByOrderId(1)).thenReturn(List.of(sampleAlloc));
        List<BlockAllocation> result = controller.getByOrderId(1);
        assertEquals(1, result.size());
        assertEquals(sampleAlloc, result.get(0));
        verify(service).findByOrderId(1);
    }

    @Test
    void createBlockAllocation_CreatesAndReturnsAlloc() {
        when(service.save(sampleAlloc)).thenReturn(sampleAlloc);
        BlockAllocation result = controller.createBlockAllocation(sampleAlloc);
        assertEquals(sampleAlloc, result);
        verify(service).save(sampleAlloc);
    }

    @Test
    void updateBlockAllocation_WhenExists_UpdatesAndReturnsAlloc() {
        when(service.update(1, sampleAlloc)).thenReturn(sampleAlloc);
        BlockAllocation result = controller.updateBlockAllocation(1, sampleAlloc);
        assertEquals(sampleAlloc, result);
        verify(service).update(1, sampleAlloc);
    }

    @Test
    void updateBlockAllocation_WhenNotExists_ThrowsException() {
        when(service.update(1, sampleAlloc)).thenThrow(new EntityNotFoundException("Not found"));
        assertThrows(EntityNotFoundException.class,
                () -> controller.updateBlockAllocation(1, sampleAlloc));
        verify(service).update(1, sampleAlloc);
    }

    @Test
    void deleteBlockAllocation_WhenExistsAndVersionMatches_DeletesAlloc() {
        doNothing().when(service).delete(1, 1);
        controller.deleteBlockAllocation(1, 1);
        verify(service).delete(1, 1);
    }

    @Test
    void deleteBlockAllocation_WhenNotExists_ThrowsException() {
        doThrow(new EntityNotFoundException("Not found")).when(service).delete(1, 1);
        assertThrows(EntityNotFoundException.class,
                () -> controller.deleteBlockAllocation(1, 1));
        verify(service).delete(1, 1);
    }

    @Test
    void deleteBlockAllocation_WhenVersionMismatch_ThrowsException() {
        doThrow(new ObjectOptimisticLockingFailureException(BlockAllocation.class, 1)).when(service).delete(1, 2);
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> controller.deleteBlockAllocation(1, 2));
        verify(service).delete(1, 2);
    }

    @Test
    void fillQuantity_Success() {
        when(service.fillQuantity(1, new BigDecimal("10.00"), 1)).thenReturn(sampleAlloc);
        BlockAllocation result = controller.fillQuantity(1, new BigDecimal("10.00"), 1);
        assertEquals(sampleAlloc, result);
        verify(service).fillQuantity(1, new BigDecimal("10.00"), 1);
    }

    @Test
    void fillQuantity_NotFound_ThrowsException() {
        when(service.fillQuantity(1, new BigDecimal("10.00"), 1)).thenThrow(new EntityNotFoundException("Not found"));
        assertThrows(EntityNotFoundException.class,
                () -> controller.fillQuantity(1, new BigDecimal("10.00"), 1));
        verify(service).fillQuantity(1, new BigDecimal("10.00"), 1);
    }

    @Test
    void fillQuantity_VersionMismatch_ThrowsException() {
        when(service.fillQuantity(1, new BigDecimal("10.00"), 2)).thenThrow(new ObjectOptimisticLockingFailureException(BlockAllocation.class, 1));
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> controller.fillQuantity(1, new BigDecimal("10.00"), 2));
        verify(service).fillQuantity(1, new BigDecimal("10.00"), 2);
    }

    @Test
    void fillQuantity_Invalid_ThrowsException() {
        when(service.fillQuantity(1, new BigDecimal("1000.00"), 1)).thenThrow(new IllegalArgumentException("Invalid"));
        assertThrows(IllegalArgumentException.class,
                () -> controller.fillQuantity(1, new BigDecimal("1000.00"), 1));
        verify(service).fillQuantity(1, new BigDecimal("1000.00"), 1);
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
                new ObjectOptimisticLockingFailureException(BlockAllocation.class, 1);
        ResponseEntity<String> response = controller.handleOptimisticLock(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().contains("modified by another user"));
    }

    @Test
    void handleIllegalArgument_ReturnsBadRequestResponse() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid");
        ResponseEntity<String> response = controller.handleIllegalArgument(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid", response.getBody());
    }
} 