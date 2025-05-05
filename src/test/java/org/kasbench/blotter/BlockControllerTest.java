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
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

@ExtendWith(MockitoExtension.class)
class BlockControllerTest {
    @Mock
    private BlockService service;
    @Mock
    private SecurityRepository securityRepository;
    @Mock
    private OrderTypeRepository orderTypeRepository;
    private BlockController controller;
    private Block sampleBlock;
    private Security sampleSecurity;
    private OrderType sampleOrderType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new BlockController(service, securityRepository, orderTypeRepository);
        sampleSecurity = new Security();
        sampleSecurity.setId(1);
        sampleOrderType = new OrderType();
        sampleOrderType.setId(2);
        sampleBlock = new Block(1, sampleSecurity, sampleOrderType, 1);
    }

    @Test
    void getAllBlocks_ReturnsAll() {
        when(service.findAll()).thenReturn(List.of(sampleBlock));
        List<Block> result = controller.getAllBlocks();
        assertEquals(1, result.size());
        assertEquals(sampleBlock, result.get(0));
        verify(service).findAll();
    }

    @Test
    void getBlockById_WhenExists_ReturnsBlock() {
        when(service.findById(1)).thenReturn(Optional.of(sampleBlock));
        ResponseEntity<Block> response = controller.getBlockById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleBlock, response.getBody());
        verify(service).findById(1);
    }

    @Test
    void getBlockById_WhenNotExists_ReturnsNotFound() {
        when(service.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<Block> response = controller.getBlockById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service).findById(1);
    }

    @Test
    void createBlock_CreatesAndReturnsBlock() {
        BlockRequestDTO dto = new BlockRequestDTO();
        dto.setSecurityId(1);
        dto.setOrderTypeId(2);
        dto.setVersion(1);
        when(securityRepository.findById(1)).thenReturn(Optional.of(sampleSecurity));
        when(orderTypeRepository.findById(2)).thenReturn(Optional.of(sampleOrderType));
        when(service.save(any(Block.class))).thenReturn(sampleBlock);
        Block result = controller.createBlock(dto);
        assertEquals(sampleBlock, result);
        verify(securityRepository).findById(1);
        verify(orderTypeRepository).findById(2);
        verify(service).save(any(Block.class));
    }

    @Test
    void updateBlock_WhenExists_UpdatesAndReturnsBlock() {
        BlockRequestDTO dto = new BlockRequestDTO();
        dto.setSecurityId(1);
        dto.setOrderTypeId(2);
        dto.setVersion(1);
        when(service.findById(1)).thenReturn(Optional.of(sampleBlock));
        when(securityRepository.findById(1)).thenReturn(Optional.of(sampleSecurity));
        when(orderTypeRepository.findById(2)).thenReturn(Optional.of(sampleOrderType));
        when(service.update(eq(1), any(Block.class))).thenReturn(sampleBlock);
        Block result = controller.updateBlock(1, dto);
        assertEquals(sampleBlock, result);
        verify(service).findById(1);
        verify(securityRepository).findById(1);
        verify(orderTypeRepository).findById(2);
        verify(service).update(eq(1), any(Block.class));
    }

    @Test
    void updateBlock_WhenNotExists_ThrowsException() {
        BlockRequestDTO dto = new BlockRequestDTO();
        dto.setSecurityId(1);
        dto.setOrderTypeId(2);
        dto.setVersion(1);
        when(service.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> controller.updateBlock(1, dto));
        verify(service).findById(1);
    }

    @Test
    void deleteBlock_WhenExistsAndVersionMatches_DeletesBlock() {
        doNothing().when(service).delete(1, 1);
        controller.deleteBlock(1, 1);
        verify(service).delete(1, 1);
    }

    @Test
    void deleteBlock_WhenNotExists_ThrowsException() {
        doThrow(new EntityNotFoundException("Not found")).when(service).delete(1, 1);
        assertThrows(EntityNotFoundException.class,
                () -> controller.deleteBlock(1, 1));
        verify(service).delete(1, 1);
    }

    @Test
    void deleteBlock_WhenVersionMismatch_ThrowsException() {
        doThrow(new ObjectOptimisticLockingFailureException(Block.class, 1)).when(service).delete(1, 2);
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> controller.deleteBlock(1, 2));
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
                new ObjectOptimisticLockingFailureException(Block.class, 1);
        ResponseEntity<String> response = controller.handleOptimisticLock(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().contains("modified by another user"));
    }
} 