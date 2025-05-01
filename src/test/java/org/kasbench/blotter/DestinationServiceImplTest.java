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

@ExtendWith(MockitoExtension.class)
class DestinationServiceImplTest {

    @Mock
    private DestinationRepository repository;

    private DestinationServiceImpl service;
    private Destination sampleDestination;

    @BeforeEach
    void setUp() {
        service = new DestinationServiceImpl(repository);
        sampleDestination = new Destination(1, "NYSE", "New York Stock Exchange", 1);
    }

    @Test
    void findAll_ReturnsAllDestinations() {
        when(repository.findAll()).thenReturn(List.of(sampleDestination));
        List<Destination> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(sampleDestination, result.get(0));
        verify(repository).findAll();
    }

    @Test
    void findById_WhenExists_ReturnsDestination() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleDestination));
        Optional<Destination> result = service.findById(1);
        assertTrue(result.isPresent());
        assertEquals(sampleDestination, result.get());
        verify(repository).findById(1);
    }

    @Test
    void findById_WhenNotExists_ReturnsEmpty() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        Optional<Destination> result = service.findById(1);
        assertFalse(result.isPresent());
        verify(repository).findById(1);
    }

    @Test
    void save_SavesDestination() {
        when(repository.save(sampleDestination)).thenReturn(sampleDestination);
        Destination result = service.save(sampleDestination);
        assertEquals(sampleDestination, result);
        verify(repository).save(sampleDestination);
    }

    @Test
    void update_WhenExists_UpdatesDestination() {
        Destination updated = new Destination(1, "NASDAQ", "NASDAQ", 1);
        when(repository.findById(1)).thenReturn(Optional.of(sampleDestination));
        when(repository.save(any(Destination.class))).thenReturn(updated);
        Destination result = service.update(1, updated);
        assertEquals("NASDAQ", result.getAbbreviation());
        assertEquals("NASDAQ", result.getDescription());
        verify(repository).findById(1);
        verify(repository).save(any(Destination.class));
    }

    @Test
    void update_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.update(1, sampleDestination));
        verify(repository).findById(1);
        verify(repository, never()).save(any(Destination.class));
    }

    @Test
    void delete_WhenExistsAndVersionMatches_DeletesDestination() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleDestination));
        service.delete(1, 1);
        verify(repository).findById(1);
        verify(repository).delete(sampleDestination);
    }

    @Test
    void delete_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.delete(1, 1));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(Destination.class));
    }

    @Test
    void delete_WhenVersionMismatch_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleDestination));
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> service.delete(1, 2));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(Destination.class));
    }
} 