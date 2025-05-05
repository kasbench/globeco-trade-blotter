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
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class TradeServiceImplTest {
    @Mock
    private TradeRepository repository;
    @Mock
    private BlockAllocationService blockAllocationService;
    private TradeServiceImpl service;
    private Trade sampleTrade;
    private Block sampleBlock;
    private TradeType sampleTradeType;
    private BlockAllocation alloc1;
    private BlockAllocation alloc2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new TradeServiceImpl(repository, blockAllocationService);
        sampleBlock = new Block();
        sampleBlock.setId(1);
        sampleTradeType = new TradeType();
        sampleTradeType.setId(2);
        sampleTrade = new Trade(1, sampleBlock, new BigDecimal("100.00"), sampleTradeType, null, new BigDecimal("50.00"), 1);
        alloc1 = new BlockAllocation();
        alloc1.setId(101);
        alloc1.setBlock(sampleBlock);
        alloc1.setQuantity(new BigDecimal("60.00"));
        alloc2 = new BlockAllocation();
        alloc2.setId(102);
        alloc2.setBlock(sampleBlock);
        alloc2.setQuantity(new BigDecimal("40.00"));
    }

    @Test
    void findAll_ReturnsAll() {
        when(repository.findAll()).thenReturn(List.of(sampleTrade));
        List<Trade> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(sampleTrade, result.get(0));
        verify(repository).findAll();
    }

    @Test
    void findById_WhenExists_ReturnsTrade() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleTrade));
        Optional<Trade> result = service.findById(1);
        assertTrue(result.isPresent());
        assertEquals(sampleTrade, result.get());
        verify(repository).findById(1);
    }

    @Test
    void findById_WhenNotExists_ReturnsEmpty() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        Optional<Trade> result = service.findById(1);
        assertFalse(result.isPresent());
        verify(repository).findById(1);
    }

    @Test
    void findByBlockId_ReturnsList() {
        when(repository.findByBlock_Id(1)).thenReturn(List.of(sampleTrade));
        List<Trade> result = service.findByBlockId(1);
        assertEquals(1, result.size());
        assertEquals(sampleTrade, result.get(0));
        verify(repository).findByBlock_Id(1);
    }

    @Test
    void save_Valid_SavesTrade() {
        when(repository.save(sampleTrade)).thenReturn(sampleTrade);
        Trade result = service.save(sampleTrade);
        assertEquals(sampleTrade, result);
        verify(repository).save(sampleTrade);
    }

    @Test
    void save_InvalidQuantity_Throws() {
        Trade trade = new Trade(2, sampleBlock, new BigDecimal("0.00"), sampleTradeType, null, BigDecimal.ZERO, 1);
        assertThrows(IllegalArgumentException.class, () -> service.save(trade));
    }

    @Test
    void save_InvalidFilled_Throws() {
        Trade trade = new Trade(2, sampleBlock, new BigDecimal("10.00"), sampleTradeType, null, new BigDecimal("-1.00"), 1);
        assertThrows(IllegalArgumentException.class, () -> service.save(trade));
    }

    @Test
    void save_FilledGreaterThanQuantity_Throws() {
        Trade trade = new Trade(2, sampleBlock, new BigDecimal("10.00"), sampleTradeType, null, new BigDecimal("20.00"), 1);
        assertThrows(IllegalArgumentException.class, () -> service.save(trade));
    }

    @Test
    void update_WhenExistsAndVersionMatches_UpdatesTrade() {
        Trade updated = new Trade(1, sampleBlock, new BigDecimal("100.00"), sampleTradeType, null, new BigDecimal("60.00"), 1);
        when(repository.findById(1)).thenReturn(Optional.of(sampleTrade));
        when(repository.save(any(Trade.class))).thenReturn(updated);
        Trade result = service.update(1, updated);
        assertEquals(updated, result);
        verify(repository).findById(1);
        verify(repository).save(any(Trade.class));
    }

    @Test
    void update_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.update(1, sampleTrade));
        verify(repository).findById(1);
        verify(repository, never()).save(any(Trade.class));
    }

    @Test
    void update_WhenVersionMismatch_ThrowsException() {
        Trade updated = new Trade(1, sampleBlock, new BigDecimal("100.00"), sampleTradeType, null, new BigDecimal("60.00"), 2);
        when(repository.findById(1)).thenReturn(Optional.of(sampleTrade));
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> service.update(1, updated));
        verify(repository).findById(1);
        verify(repository, never()).save(any(Trade.class));
    }

    @Test
    void update_InvalidTrade_Throws() {
        Trade updated = new Trade(1, sampleBlock, new BigDecimal("0.00"), sampleTradeType, null, BigDecimal.ZERO, 1);
        when(repository.findById(1)).thenReturn(Optional.of(sampleTrade));
        assertThrows(IllegalArgumentException.class, () -> service.update(1, updated));
    }

    @Test
    void delete_WhenExistsAndVersionMatches_DeletesTrade() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleTrade));
        service.delete(1, 1);
        verify(repository).findById(1);
        verify(repository).delete(sampleTrade);
    }

    @Test
    void delete_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.delete(1, 1));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(Trade.class));
    }

    @Test
    void delete_WhenVersionMismatch_ThrowsException() {
        Trade tradeWithDiffVersion = new Trade(1, sampleBlock, new BigDecimal("100.00"), sampleTradeType, null, new BigDecimal("50.00"), 2);
        when(repository.findById(1)).thenReturn(Optional.of(tradeWithDiffVersion));
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> service.delete(1, 1));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(Trade.class));
    }

    @Test
    void fillQuantity_Valid_AddsFilled() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleTrade));
        when(repository.save(any(Trade.class))).thenReturn(sampleTrade);
        Trade result = service.fillQuantity(1, new BigDecimal("10.00"), 1);
        assertNotNull(result);
        verify(repository).findById(1);
        verify(repository).save(any(Trade.class));
    }

    @Test
    void fillQuantity_ExceedsQuantity_Throws() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleTrade));
        assertThrows(IllegalArgumentException.class, () -> service.fillQuantity(1, new BigDecimal("100.00"), 1));
    }

    @Test
    void fillQuantity_NegativeResult_Throws() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleTrade));
        assertThrows(IllegalArgumentException.class, () -> service.fillQuantity(1, new BigDecimal("-100.00"), 1));
    }

    @Test
    void fillQuantity_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.fillQuantity(1, new BigDecimal("10.00"), 1));
    }

    @Test
    void fillQuantity_WhenVersionMismatch_ThrowsException() {
        Trade tradeWithDiffVersion = new Trade(1, sampleBlock, new BigDecimal("100.00"), sampleTradeType, null, new BigDecimal("50.00"), 2);
        when(repository.findById(1)).thenReturn(Optional.of(tradeWithDiffVersion));
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> service.fillQuantity(1, new BigDecimal("10.00"), 1));
    }

    @Test
    void allocateProRata_Success() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleTrade));
        when(blockAllocationService.findByBlockId(1)).thenReturn(Arrays.asList(alloc1, alloc2));
        Trade result = service.allocateProRata(1, 1);
        assertNotNull(result);
        verify(blockAllocationService, times(2)).update(anyInt(), any(BlockAllocation.class));
        // Check allocations sum to trade filledQuantity
        BigDecimal sum = alloc1.getFilledQuantity().add(alloc2.getFilledQuantity());
        assertEquals(sampleTrade.getFilledQuantity().setScale(8), sum.setScale(8));
    }

    @Test
    void allocateProRata_TradeNotFound_Throws() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.allocateProRata(1, 1));
    }

    @Test
    void allocateProRata_VersionMismatch_Throws() {
        Trade tradeDiffVersion = new Trade(1, sampleBlock, new BigDecimal("100.00"), sampleTradeType, null, new BigDecimal("50.00"), 2);
        when(repository.findById(1)).thenReturn(Optional.of(tradeDiffVersion));
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> service.allocateProRata(1, 1));
    }

    @Test
    void allocateProRata_NoAllocations_Throws() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleTrade));
        when(blockAllocationService.findByBlockId(1)).thenReturn(new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> service.allocateProRata(1, 1));
    }

    @Test
    void allocateProRata_TotalQuantityZero_Throws() {
        alloc1.setQuantity(BigDecimal.ZERO);
        alloc2.setQuantity(BigDecimal.ZERO);
        when(repository.findById(1)).thenReturn(Optional.of(sampleTrade));
        when(blockAllocationService.findByBlockId(1)).thenReturn(Arrays.asList(alloc1, alloc2));
        assertThrows(IllegalArgumentException.class, () -> service.allocateProRata(1, 1));
    }
} 