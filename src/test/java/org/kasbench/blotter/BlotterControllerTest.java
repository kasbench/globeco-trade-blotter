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
class BlotterControllerTest {

    @Mock
    private BlotterService service;

    private BlotterController controller;
    private Blotter sampleBlotter;

    @BeforeEach
    void setUp() {
        controller = new BlotterController(service);
        sampleBlotter = new Blotter(1, "Default", false, null, 1);
    }

    @Test
    void getAllBlotters_ReturnsAll() {
        when(service.findAll()).thenReturn(List.of(sampleBlotter));
        List<Blotter> result = controller.getAllBlotters();
        assertEquals(1, result.size());
        assertEquals(sampleBlotter, result.get(0));
        verify(service).findAll();
    }

    @Test
    void getBlotterById_WhenExists_ReturnsBlotter() {
        when(service.findById(1)).thenReturn(Optional.of(sampleBlotter));
        ResponseEntity<Blotter> response = controller.getBlotterById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleBlotter, response.getBody());
        verify(service).findById(1);
    }

    @Test
    void getBlotterById_WhenNotExists_ReturnsNotFound() {
        when(service.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<Blotter> response = controller.getBlotterById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service).findById(1);
    }

    @Test
    void createBlotter_CreatesAndReturnsBlotter() {
        when(service.save(sampleBlotter)).thenReturn(sampleBlotter);
        Blotter result = controller.createBlotter(sampleBlotter);
        assertEquals(sampleBlotter, result);
        verify(service).save(sampleBlotter);
    }

    @Test
    void updateBlotter_WhenExists_UpdatesAndReturnsBlotter() {
        when(service.update(1, sampleBlotter)).thenReturn(sampleBlotter);
        Blotter result = controller.updateBlotter(1, sampleBlotter);
        assertEquals(sampleBlotter, result);
        verify(service).update(1, sampleBlotter);
    }

    @Test
    void updateBlotter_WhenNotExists_ThrowsException() {
        when(service.update(1, sampleBlotter)).thenThrow(new EntityNotFoundException("Not found"));
        assertThrows(EntityNotFoundException.class,
                () -> controller.updateBlotter(1, sampleBlotter));
        verify(service).update(1, sampleBlotter);
    }

    @Test
    void deleteBlotter_WhenExistsAndVersionMatches_DeletesBlotter() {
        doNothing().when(service).delete(1, 1);
        controller.deleteBlotter(1, 1);
        verify(service).delete(1, 1);
    }

    @Test
    void deleteBlotter_WhenNotExists_ThrowsException() {
        doThrow(new EntityNotFoundException("Not found")).when(service).delete(1, 1);
        assertThrows(EntityNotFoundException.class,
                () -> controller.deleteBlotter(1, 1));
        verify(service).delete(1, 1);
    }

    @Test
    void deleteBlotter_WhenVersionMismatch_ThrowsException() {
        doThrow(new ObjectOptimisticLockingFailureException(Blotter.class, 1)).when(service).delete(1, 2);
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> controller.deleteBlotter(1, 2));
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
                new ObjectOptimisticLockingFailureException(Blotter.class, 1);
        ResponseEntity<String> response = controller.handleOptimisticLock(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().contains("modified by another user"));
    }
} 