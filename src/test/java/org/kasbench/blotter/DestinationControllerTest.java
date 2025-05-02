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
class DestinationControllerTest {

    @Mock
    private DestinationService service;

    private DestinationController controller;
    private Destination sampleDestination;

    @BeforeEach
    void setUp() {
        controller = new DestinationController(service);
        sampleDestination = new Destination(1, "NYSE", "New York Stock Exchange", 1);
    }

    @Test
    void getAllDestinations_ReturnsAll() {
        when(service.findAll()).thenReturn(List.of(sampleDestination));
        List<Destination> result = controller.getAllDestinations();
        assertEquals(1, result.size());
        assertEquals(sampleDestination, result.get(0));
        verify(service).findAll();
    }

    @Test
    void getDestinationById_WhenExists_ReturnsDestination() {
        when(service.findById(1)).thenReturn(Optional.of(sampleDestination));
        ResponseEntity<Destination> response = controller.getDestinationById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleDestination, response.getBody());
        verify(service).findById(1);
    }

    @Test
    void getDestinationById_WhenNotExists_ReturnsNotFound() {
        when(service.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<Destination> response = controller.getDestinationById(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service).findById(1);
    }

    @Test
    void createDestination_CreatesAndReturnsDestination() {
        when(service.save(sampleDestination)).thenReturn(sampleDestination);
        Destination result = controller.createDestination(sampleDestination);
        assertEquals(sampleDestination, result);
        verify(service).save(sampleDestination);
    }

    @Test
    void updateDestination_WhenExists_UpdatesAndReturnsDestination() {
        when(service.update(1, sampleDestination)).thenReturn(sampleDestination);
        Destination result = controller.updateDestination(1, sampleDestination);
        assertEquals(sampleDestination, result);
        verify(service).update(1, sampleDestination);
    }

    @Test
    void updateDestination_WhenNotExists_ThrowsException() {
        when(service.update(1, sampleDestination)).thenThrow(new EntityNotFoundException("Not found"));
        assertThrows(EntityNotFoundException.class,
                () -> controller.updateDestination(1, sampleDestination));
        verify(service).update(1, sampleDestination);
    }

    @Test
    void deleteDestination_WhenExistsAndVersionMatches_DeletesDestination() {
        doNothing().when(service).delete(1, 1);
        controller.deleteDestination(1, 1);
        verify(service).delete(1, 1);
    }

    @Test
    void deleteDestination_WhenNotExists_ThrowsException() {
        doThrow(new EntityNotFoundException("Not found")).when(service).delete(1, 1);
        assertThrows(EntityNotFoundException.class,
                () -> controller.deleteDestination(1, 1));
        verify(service).delete(1, 1);
    }

    @Test
    void deleteDestination_WhenVersionMismatch_ThrowsException() {
        doThrow(new ObjectOptimisticLockingFailureException(Destination.class, 1)).when(service).delete(1, 2);
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> controller.deleteDestination(1, 2));
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
                new ObjectOptimisticLockingFailureException(Destination.class, 1);
        ResponseEntity<String> response = controller.handleOptimisticLock(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().contains("modified by another user"));
    }
} 