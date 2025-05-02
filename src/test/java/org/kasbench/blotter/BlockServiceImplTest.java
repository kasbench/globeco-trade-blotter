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
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class BlockServiceImplTest {
    @Mock
    private BlockRepository repository;
    @Mock
    private OrderService orderService;
    @Mock
    private BlockAllocationService blockAllocationService;
    private BlockServiceImpl service;
    private Block sampleBlock;
    private Security sampleSecurity;
    private OrderType sampleOrderType;
    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new BlockServiceImpl(repository, orderService, blockAllocationService);
        sampleSecurity = new Security();
        sampleSecurity.setId(1);
        sampleOrderType = new OrderType();
        sampleOrderType.setId(2);
        sampleBlock = new Block(1, sampleSecurity, sampleOrderType, 1);
        sampleOrder = new Order();
        sampleOrder.setId(10);
        sampleOrder.setSecurity(sampleSecurity);
        sampleOrder.setOrderType(sampleOrderType);
        sampleOrder.setQuantity(new BigDecimal("100.00"));
    }

    @Test
    void findAll_ReturnsAllBlocks() {
        when(repository.findAll()).thenReturn(List.of(sampleBlock));
        List<Block> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(sampleBlock, result.get(0));
        verify(repository).findAll();
    }

    @Test
    void findById_WhenExists_ReturnsBlock() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleBlock));
        Optional<Block> result = service.findById(1);
        assertTrue(result.isPresent());
        assertEquals(sampleBlock, result.get());
        verify(repository).findById(1);
    }

    @Test
    void findById_WhenNotExists_ReturnsEmpty() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        Optional<Block> result = service.findById(1);
        assertFalse(result.isPresent());
        verify(repository).findById(1);
    }

    @Test
    void save_SavesBlock() {
        when(repository.save(sampleBlock)).thenReturn(sampleBlock);
        Block result = service.save(sampleBlock);
        assertEquals(sampleBlock, result);
        verify(repository).save(sampleBlock);
    }

    @Test
    void update_WhenExists_UpdatesBlock() {
        Block updated = new Block(1, sampleSecurity, sampleOrderType, 1);
        when(repository.findById(1)).thenReturn(Optional.of(sampleBlock));
        when(repository.save(any(Block.class))).thenReturn(updated);
        Block result = service.update(1, updated);
        assertEquals(updated, result);
        verify(repository).findById(1);
        verify(repository).save(any(Block.class));
    }

    @Test
    void update_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.update(1, sampleBlock));
        verify(repository).findById(1);
        verify(repository, never()).save(any(Block.class));
    }

    @Test
    void delete_WhenExistsAndVersionMatches_DeletesBlock() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleBlock));
        service.delete(1, 1);
        verify(repository).findById(1);
        verify(repository).delete(sampleBlock);
    }

    @Test
    void delete_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.delete(1, 1));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(Block.class));
    }

    @Test
    void delete_WhenVersionMismatch_ThrowsException() {
        Block blockWithDiffVersion = new Block(1, sampleSecurity, sampleOrderType, 2);
        when(repository.findById(1)).thenReturn(Optional.of(blockWithDiffVersion));
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> service.delete(1, 1));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(Block.class));
    }

    @Test
    void createBlockWithAllocations_Success() {
        when(orderService.findById(10)).thenReturn(Optional.of(sampleOrder));
        when(repository.save(any(Block.class))).thenAnswer(invocation -> {
            Block b = invocation.getArgument(0);
            b.setId(99);
            return b;
        });
        Block result = service.createBlockWithAllocations(List.of(10));
        assertNotNull(result);
        assertEquals(sampleSecurity, result.getSecurity());
        assertEquals(sampleOrderType, result.getOrderType());
        verify(orderService).findById(10);
        verify(repository).save(any(Block.class));
        verify(blockAllocationService).save(any(BlockAllocation.class));
    }

    @Test
    void createBlockWithAllocations_DifferentSecurity_Throws() {
        Order order2 = new Order();
        order2.setId(11);
        Security sec2 = new Security();
        sec2.setId(2);
        order2.setSecurity(sec2);
        order2.setOrderType(sampleOrderType);
        order2.setQuantity(new BigDecimal("50.00"));
        when(orderService.findById(10)).thenReturn(Optional.of(sampleOrder));
        when(orderService.findById(11)).thenReturn(Optional.of(order2));
        assertThrows(IllegalArgumentException.class, () ->
            service.createBlockWithAllocations(Arrays.asList(10, 11)));
    }

    @Test
    void createBlockWithAllocations_DifferentOrderType_Throws() {
        Order order2 = new Order();
        order2.setId(11);
        order2.setSecurity(sampleSecurity);
        OrderType ot2 = new OrderType();
        ot2.setId(3);
        order2.setOrderType(ot2);
        order2.setQuantity(new BigDecimal("50.00"));
        when(orderService.findById(10)).thenReturn(Optional.of(sampleOrder));
        when(orderService.findById(11)).thenReturn(Optional.of(order2));
        assertThrows(IllegalArgumentException.class, () ->
            service.createBlockWithAllocations(Arrays.asList(10, 11)));
    }

    @Test
    void createBlockWithAllocations_OrderNotFound_Throws() {
        when(orderService.findById(10)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () ->
            service.createBlockWithAllocations(List.of(10)));
    }

    @Test
    void createBlockWithAllocations_EmptyList_Throws() {
        assertThrows(IllegalArgumentException.class, () ->
            service.createBlockWithAllocations(List.of()));
    }
} 