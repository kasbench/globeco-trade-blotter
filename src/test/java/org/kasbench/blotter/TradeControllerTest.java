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
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

@ExtendWith(MockitoExtension.class)
class TradeControllerTest {
    @Mock
    private TradeService service;
    @Mock
    private BlockRepository blockRepository;
    @Mock
    private TradeTypeRepository tradeTypeRepository;
    private TradeController controller;
    private Trade sampleTrade;
    private Block sampleBlock;
    private TradeType sampleTradeType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new TradeController(service, blockRepository, tradeTypeRepository);
        sampleBlock = new Block();
        sampleBlock.setId(1);
        sampleTradeType = new TradeType();
        sampleTradeType.setId(2);
        sampleTrade = new Trade(1, sampleBlock, new BigDecimal("100.00"), sampleTradeType, new BigDecimal("50.00"), 1);
    }

    @Test
    void getAllTrades_ReturnsAll() {
        when(service.findAll()).thenReturn(List.of(sampleTrade));
        List<Trade> result = controller.getAllTrades();
        assertEquals(1, result.size());
        assertEquals(sampleTrade, result.get(0));
        verify(service).findAll();
    }

    @Test
    void getTradeById_WhenExists_ReturnsTrade() {
        when(service.findById(1)).thenReturn(Optional.of(sampleTrade));
        ResponseEntity<Trade> response = controller.getTradeById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleTrade, response.getBody());
        verify(service).findById(1);
    }

    @Test
    void getTradeById_WhenNotExists_ReturnsNotFound() {
        when(service.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<Trade> response = controller.getTradeById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service).findById(1);
    }

    @Test
    void getByBlockId_ReturnsList() {
        when(service.findByBlockId(1)).thenReturn(List.of(sampleTrade));
        List<Trade> result = controller.getByBlockId(1);
        assertEquals(1, result.size());
        assertEquals(sampleTrade, result.get(0));
        verify(service).findByBlockId(1);
    }

    @Test
    void createTrade_CreatesAndReturnsTrade() {
        TradeRequestDTO dto = new TradeRequestDTO();
        dto.setBlockId(1);
        dto.setQuantity(new BigDecimal("100.00"));
        dto.setTradeTypeId(2);
        dto.setFilledQuantity(new BigDecimal("50.00"));
        dto.setVersion(1);
        when(blockRepository.findById(1)).thenReturn(Optional.of(sampleBlock));
        when(tradeTypeRepository.findById(2)).thenReturn(Optional.of(sampleTradeType));
        when(service.save(any(Trade.class))).thenReturn(sampleTrade);
        Trade result = controller.createTrade(dto);
        assertEquals(sampleTrade, result);
        verify(blockRepository).findById(1);
        verify(tradeTypeRepository).findById(2);
        verify(service).save(any(Trade.class));
    }

    @Test
    void updateTrade_WhenExists_UpdatesAndReturnsTrade() {
        TradeRequestDTO dto = new TradeRequestDTO();
        dto.setBlockId(1);
        dto.setQuantity(new BigDecimal("100.00"));
        dto.setTradeTypeId(2);
        dto.setFilledQuantity(new BigDecimal("50.00"));
        dto.setVersion(1);
        when(service.findById(1)).thenReturn(Optional.of(sampleTrade));
        when(blockRepository.findById(1)).thenReturn(Optional.of(sampleBlock));
        when(tradeTypeRepository.findById(2)).thenReturn(Optional.of(sampleTradeType));
        when(service.update(eq(1), any(Trade.class))).thenReturn(sampleTrade);
        Trade result = controller.updateTrade(1, dto);
        assertEquals(sampleTrade, result);
        verify(service).findById(1);
        verify(blockRepository).findById(1);
        verify(tradeTypeRepository).findById(2);
        verify(service).update(eq(1), any(Trade.class));
    }

    @Test
    void updateTrade_WhenNotExists_ThrowsException() {
        TradeRequestDTO dto = new TradeRequestDTO();
        dto.setBlockId(1);
        dto.setQuantity(new BigDecimal("100.00"));
        dto.setTradeTypeId(2);
        dto.setFilledQuantity(new BigDecimal("50.00"));
        dto.setVersion(1);
        when(service.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> controller.updateTrade(1, dto));
        verify(service).findById(1);
    }

    @Test
    void deleteTrade_WhenExistsAndVersionMatches_DeletesTrade() {
        doNothing().when(service).delete(1, 1);
        controller.deleteTrade(1, 1);
        verify(service).delete(1, 1);
    }

    @Test
    void deleteTrade_WhenNotExists_ThrowsException() {
        doThrow(new EntityNotFoundException("Not found")).when(service).delete(1, 1);
        assertThrows(EntityNotFoundException.class,
                () -> controller.deleteTrade(1, 1));
        verify(service).delete(1, 1);
    }

    @Test
    void deleteTrade_WhenVersionMismatch_ThrowsException() {
        doThrow(new ObjectOptimisticLockingFailureException(Trade.class, 1)).when(service).delete(1, 2);
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> controller.deleteTrade(1, 2));
        verify(service).delete(1, 2);
    }

    @Test
    void fillQuantity_Success() {
        when(service.fillQuantity(1, new BigDecimal("10.00"), 1)).thenReturn(sampleTrade);
        Trade result = controller.fillQuantity(1, new BigDecimal("10.00"), 1);
        assertEquals(sampleTrade, result);
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
        when(service.fillQuantity(1, new BigDecimal("10.00"), 2)).thenThrow(new ObjectOptimisticLockingFailureException(Trade.class, 1));
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
                new ObjectOptimisticLockingFailureException(Trade.class, 1);
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