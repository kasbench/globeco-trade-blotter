package org.kasbench.blotter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlockAllocationServiceImplTest {
    @Mock
    private BlockAllocationRepository repository;
    private BlockAllocationServiceImpl service;
    private BlockAllocation sampleAlloc;
    private Order sampleOrder;
    private Block sampleBlock;

    @BeforeEach
    void setUp() {
        service = new BlockAllocationServiceImpl(repository);
        sampleOrder = new Order();
        sampleOrder.setId(1);
        sampleBlock = new Block();
        sampleBlock.setId(2);
        sampleAlloc = new BlockAllocation(1, sampleOrder, sampleBlock, new BigDecimal("100.00"), new BigDecimal("50.00"), 1);
    }

    @Test
    void findAll_ReturnsAll() {
        when(repository.findAll()).thenReturn(List.of(sampleAlloc));
        List<BlockAllocation> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(sampleAlloc, result.get(0));
        verify(repository).findAll();
    }

    @Test
    void findById_WhenExists_ReturnsAlloc() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleAlloc));
        Optional<BlockAllocation> result = service.findById(1);
        assertTrue(result.isPresent());
        assertEquals(sampleAlloc, result.get());
        verify(repository).findById(1);
    }

    @Test
    void findById_WhenNotExists_ReturnsEmpty() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        Optional<BlockAllocation> result = service.findById(1);
        assertFalse(result.isPresent());
        verify(repository).findById(1);
    }

    @Test
    void findByBlockId_ReturnsList() {
        when(repository.findByBlock_Id(2)).thenReturn(List.of(sampleAlloc));
        List<BlockAllocation> result = service.findByBlockId(2);
        assertEquals(1, result.size());
        assertEquals(sampleAlloc, result.get(0));
        verify(repository).findByBlock_Id(2);
    }

    @Test
    void findByOrderId_ReturnsList() {
        when(repository.findByOrder_Id(1)).thenReturn(List.of(sampleAlloc));
        List<BlockAllocation> result = service.findByOrderId(1);
        assertEquals(1, result.size());
        assertEquals(sampleAlloc, result.get(0));
        verify(repository).findByOrder_Id(1);
    }

    @Test
    void save_Valid_SavesAlloc() {
        when(repository.save(sampleAlloc)).thenReturn(sampleAlloc);
        BlockAllocation result = service.save(sampleAlloc);
        assertEquals(sampleAlloc, result);
        verify(repository).save(sampleAlloc);
    }

    @Test
    void save_InvalidQuantity_Throws() {
        BlockAllocation alloc = new BlockAllocation(2, sampleOrder, sampleBlock, new BigDecimal("0.00"), BigDecimal.ZERO, 1);
        assertThrows(IllegalArgumentException.class, () -> service.save(alloc));
    }

    @Test
    void save_InvalidFilled_Throws() {
        BlockAllocation alloc = new BlockAllocation(2, sampleOrder, sampleBlock, new BigDecimal("10.00"), new BigDecimal("-1.00"), 1);
        assertThrows(IllegalArgumentException.class, () -> service.save(alloc));
    }

    @Test
    void save_FilledGreaterThanQuantity_Throws() {
        BlockAllocation alloc = new BlockAllocation(2, sampleOrder, sampleBlock, new BigDecimal("10.00"), new BigDecimal("20.00"), 1);
        assertThrows(IllegalArgumentException.class, () -> service.save(alloc));
    }

    @Test
    void update_WhenExistsAndVersionMatches_UpdatesAlloc() {
        BlockAllocation updated = new BlockAllocation(1, sampleOrder, sampleBlock, new BigDecimal("100.00"), new BigDecimal("60.00"), 1);
        when(repository.findById(1)).thenReturn(Optional.of(sampleAlloc));
        when(repository.save(any(BlockAllocation.class))).thenReturn(updated);
        BlockAllocation result = service.update(1, updated);
        assertEquals(updated, result);
        verify(repository).findById(1);
        verify(repository).save(any(BlockAllocation.class));
    }

    @Test
    void update_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.update(1, sampleAlloc));
        verify(repository).findById(1);
        verify(repository, never()).save(any(BlockAllocation.class));
    }

    @Test
    void update_WhenVersionMismatch_ThrowsException() {
        BlockAllocation updated = new BlockAllocation(1, sampleOrder, sampleBlock, new BigDecimal("100.00"), new BigDecimal("60.00"), 2);
        when(repository.findById(1)).thenReturn(Optional.of(sampleAlloc));
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> service.update(1, updated));
        verify(repository).findById(1);
        verify(repository, never()).save(any(BlockAllocation.class));
    }

    @Test
    void update_InvalidAllocation_Throws() {
        BlockAllocation updated = new BlockAllocation(1, sampleOrder, sampleBlock, new BigDecimal("0.00"), BigDecimal.ZERO, 1);
        when(repository.findById(1)).thenReturn(Optional.of(sampleAlloc));
        assertThrows(IllegalArgumentException.class, () -> service.update(1, updated));
    }

    @Test
    void delete_WhenExistsAndVersionMatches_DeletesAlloc() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleAlloc));
        service.delete(1, 1);
        verify(repository).findById(1);
        verify(repository).delete(sampleAlloc);
    }

    @Test
    void delete_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.delete(1, 1));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(BlockAllocation.class));
    }

    @Test
    void delete_WhenVersionMismatch_ThrowsException() {
        BlockAllocation allocWithDiffVersion = new BlockAllocation(1, sampleOrder, sampleBlock, new BigDecimal("100.00"), new BigDecimal("50.00"), 2);
        when(repository.findById(1)).thenReturn(Optional.of(allocWithDiffVersion));
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> service.delete(1, 1));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(BlockAllocation.class));
    }

    @Test
    void fillQuantity_Valid_AddsFilled() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleAlloc));
        when(repository.save(any(BlockAllocation.class))).thenReturn(sampleAlloc);
        BlockAllocation result = service.fillQuantity(1, new BigDecimal("10.00"), 1);
        assertNotNull(result);
        verify(repository).findById(1);
        verify(repository).save(any(BlockAllocation.class));
    }

    @Test
    void fillQuantity_ExceedsQuantity_Throws() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleAlloc));
        assertThrows(IllegalArgumentException.class, () -> service.fillQuantity(1, new BigDecimal("100.00"), 1));
    }

    @Test
    void fillQuantity_NegativeResult_Throws() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleAlloc));
        assertThrows(IllegalArgumentException.class, () -> service.fillQuantity(1, new BigDecimal("-100.00"), 1));
    }

    @Test
    void fillQuantity_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.fillQuantity(1, new BigDecimal("10.00"), 1));
    }

    @Test
    void fillQuantity_WhenVersionMismatch_ThrowsException() {
        BlockAllocation allocWithDiffVersion = new BlockAllocation(1, sampleOrder, sampleBlock, new BigDecimal("100.00"), new BigDecimal("50.00"), 2);
        when(repository.findById(1)).thenReturn(Optional.of(allocWithDiffVersion));
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> service.fillQuantity(1, new BigDecimal("10.00"), 1));
    }
} 