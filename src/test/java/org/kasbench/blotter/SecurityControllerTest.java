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
class SecurityControllerTest {

    @Mock
    private SecurityService service;

    private SecurityController controller;
    private Security sampleSecurity;
    private SecurityType sampleSecurityType;

    @BeforeEach
    void setUp() {
        controller = new SecurityController(service);
        sampleSecurityType = new SecurityType(1, "STK", "Stock", 1);
        sampleSecurity = new Security(1, "IBM", "IBM Corporation", sampleSecurityType, 1);
    }

    @Test
    void getAllSecurities_ReturnsAll() {
        when(service.findAll()).thenReturn(List.of(sampleSecurity));
        List<Security> result = controller.getAllSecurities();
        assertEquals(1, result.size());
        assertEquals(sampleSecurity, result.get(0));
        verify(service).findAll();
    }

    @Test
    void getSecurityById_WhenExists_ReturnsSecurity() {
        when(service.findById(1)).thenReturn(Optional.of(sampleSecurity));
        ResponseEntity<Security> response = controller.getSecurityById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleSecurity, response.getBody());
        verify(service).findById(1);
    }

    @Test
    void getSecurityById_WhenNotExists_ReturnsNotFound() {
        when(service.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<Security> response = controller.getSecurityById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service).findById(1);
    }

    @Test
    void createSecurity_CreatesAndReturnsSecurity() {
        when(service.save(sampleSecurity)).thenReturn(sampleSecurity);
        Security result = controller.createSecurity(sampleSecurity);
        assertEquals(sampleSecurity, result);
        verify(service).save(sampleSecurity);
    }

    @Test
    void updateSecurity_WhenExists_UpdatesAndReturnsSecurity() {
        when(service.update(1, sampleSecurity)).thenReturn(sampleSecurity);
        Security result = controller.updateSecurity(1, sampleSecurity);
        assertEquals(sampleSecurity, result);
        verify(service).update(1, sampleSecurity);
    }

    @Test
    void updateSecurity_WhenNotExists_ThrowsException() {
        when(service.update(1, sampleSecurity)).thenThrow(new EntityNotFoundException("Not found"));
        assertThrows(EntityNotFoundException.class,
                () -> controller.updateSecurity(1, sampleSecurity));
        verify(service).update(1, sampleSecurity);
    }

    @Test
    void deleteSecurity_WhenExistsAndVersionMatches_DeletesSecurity() {
        doNothing().when(service).delete(1, 1);
        controller.deleteSecurity(1, 1);
        verify(service).delete(1, 1);
    }

    @Test
    void deleteSecurity_WhenNotExists_ThrowsException() {
        doThrow(new EntityNotFoundException("Not found")).when(service).delete(1, 1);
        assertThrows(EntityNotFoundException.class,
                () -> controller.deleteSecurity(1, 1));
        verify(service).delete(1, 1);
    }

    @Test
    void deleteSecurity_WhenVersionMismatch_ThrowsException() {
        doThrow(new ObjectOptimisticLockingFailureException(Security.class, 1)).when(service).delete(1, 2);
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> controller.deleteSecurity(1, 2));
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
                new ObjectOptimisticLockingFailureException(Security.class, 1);
        ResponseEntity<String> response = controller.handleOptimisticLock(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().contains("modified by another user"));
    }
} 