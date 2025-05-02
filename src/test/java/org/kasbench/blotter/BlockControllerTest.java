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
class BlockControllerTest {
    @Mock
    private BlockService service;
    private BlockController controller;
    private Block sampleBlock;
    private Security sampleSecurity;
    private OrderType sampleOrderType;

    @BeforeEach
    void setUp() {
        controller = new BlockController(service);
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
        when(service.save(sampleBlock)).thenReturn(sampleBlock);
        Block result = controller.createBlock(sampleBlock);
        assertEquals(sampleBlock, result);
        verify(service).save(sampleBlock);
    }

    @Test
    void updateBlock_WhenExists_UpdatesAndReturnsBlock() {
        when(service.update(1, sampleBlock)).thenReturn(sampleBlock);
        Block result = controller.updateBlock(1, sampleBlock);
        assertEquals(sampleBlock, result);
        verify(service).update(1, sampleBlock);
    }

    @Test
    void updateBlock_WhenNotExists_ThrowsException() {
        when(service.update(1, sampleBlock)).thenThrow(new EntityNotFoundException("Not found"));
        assertThrows(EntityNotFoundException.class,
                () -> controller.updateBlock(1, sampleBlock));
        verify(service).update(1, sampleBlock);
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