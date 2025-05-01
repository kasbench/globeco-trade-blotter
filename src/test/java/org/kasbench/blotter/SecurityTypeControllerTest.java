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
class SecurityTypeControllerTest {

    @Mock
    private SecurityTypeService service;

    private SecurityTypeController controller;
    private SecurityType sampleSecurityType;

    @BeforeEach
    void setUp() {
        controller = new SecurityTypeController(service);
        sampleSecurityType = new SecurityType(1, "CS", "Common Stock", 1);
    }

    @Test
    void getAllSecurityTypes_ReturnsAllTypes() {
        when(service.findAll()).thenReturn(List.of(sampleSecurityType));

        List<SecurityType> result = controller.getAllSecurityTypes();

        assertEquals(1, result.size());
        assertEquals(sampleSecurityType, result.get(0));
        verify(service).findAll();
    }

    @Test
    void getSecurityTypeById_WhenExists_ReturnsType() {
        when(service.findById(1)).thenReturn(Optional.of(sampleSecurityType));

        ResponseEntity<SecurityType> response = controller.getSecurityTypeById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleSecurityType, response.getBody());
        verify(service).findById(1);
    }

    @Test
    void getSecurityTypeById_WhenNotExists_ReturnsNotFound() {
        when(service.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<SecurityType> response = controller.getSecurityTypeById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service).findById(1);
    }

    @Test
    void createSecurityType_CreatesAndReturnsType() {
        when(service.save(sampleSecurityType)).thenReturn(sampleSecurityType);

        SecurityType result = controller.createSecurityType(sampleSecurityType);

        assertEquals(sampleSecurityType, result);
        verify(service).save(sampleSecurityType);
    }

    @Test
    void updateSecurityType_WhenExists_UpdatesAndReturnsType() {
        when(service.update(1, sampleSecurityType)).thenReturn(sampleSecurityType);

        SecurityType result = controller.updateSecurityType(1, sampleSecurityType);

        assertEquals(sampleSecurityType, result);
        verify(service).update(1, sampleSecurityType);
    }

    @Test
    void updateSecurityType_WhenNotExists_ThrowsException() {
        when(service.update(1, sampleSecurityType))
            .thenThrow(new EntityNotFoundException("Not found"));

        assertThrows(EntityNotFoundException.class,
                () -> controller.updateSecurityType(1, sampleSecurityType));
        verify(service).update(1, sampleSecurityType);
    }

    @Test
    void deleteSecurityType_WhenExistsAndVersionMatches_DeletesType() {
        doNothing().when(service).delete(1, 1);

        controller.deleteSecurityType(1, 1);

        verify(service).delete(1, 1);
    }

    @Test
    void deleteSecurityType_WhenNotExists_ThrowsException() {
        doThrow(new EntityNotFoundException("Not found"))
            .when(service).delete(1, 1);

        assertThrows(EntityNotFoundException.class,
                () -> controller.deleteSecurityType(1, 1));
        verify(service).delete(1, 1);
    }

    @Test
    void deleteSecurityType_WhenVersionMismatch_ThrowsException() {
        doThrow(new ObjectOptimisticLockingFailureException(SecurityType.class, 1))
            .when(service).delete(1, 2);

        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> controller.deleteSecurityType(1, 2));
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
            new ObjectOptimisticLockingFailureException(SecurityType.class, 1);
        ResponseEntity<String> response = controller.handleOptimisticLock(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().contains("modified by another user"));
    }
} 