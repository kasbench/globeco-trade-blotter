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
class BlotterServiceImplTest {

    @Mock
    private BlotterRepository repository;

    private BlotterServiceImpl service;
    private Blotter sampleBlotter;

    @BeforeEach
    void setUp() {
        service = new BlotterServiceImpl(repository);
        sampleBlotter = new Blotter(1, "Default", false, null, 1);
    }

    @Test
    void findAll_ReturnsAllBlotters() {
        when(repository.findAll()).thenReturn(List.of(sampleBlotter));
        List<Blotter> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(sampleBlotter, result.get(0));
        verify(repository).findAll();
    }

    @Test
    void findById_WhenExists_ReturnsBlotter() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleBlotter));
        Optional<Blotter> result = service.findById(1);
        assertTrue(result.isPresent());
        assertEquals(sampleBlotter, result.get());
        verify(repository).findById(1);
    }

    @Test
    void findById_WhenNotExists_ReturnsEmpty() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        Optional<Blotter> result = service.findById(1);
        assertFalse(result.isPresent());
        verify(repository).findById(1);
    }

    @Test
    void save_SavesBlotter() {
        when(repository.save(sampleBlotter)).thenReturn(sampleBlotter);
        Blotter result = service.save(sampleBlotter);
        assertEquals(sampleBlotter, result);
        verify(repository).save(sampleBlotter);
    }

    @Test
    void update_WhenExists_UpdatesBlotter() {
        Blotter updated = new Blotter(1, "Priority", true, 2, 1);
        when(repository.findById(1)).thenReturn(Optional.of(sampleBlotter));
        when(repository.save(any(Blotter.class))).thenReturn(updated);
        Blotter result = service.update(1, updated);
        assertEquals("Priority", result.getName());
        assertTrue(result.getAutoPopulate());
        assertEquals(2, result.getSecurityTypeId());
        verify(repository).findById(1);
        verify(repository).save(any(Blotter.class));
    }

    @Test
    void update_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.update(1, sampleBlotter));
        verify(repository).findById(1);
        verify(repository, never()).save(any(Blotter.class));
    }

    @Test
    void delete_WhenExistsAndVersionMatches_DeletesBlotter() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleBlotter));
        service.delete(1, 1);
        verify(repository).findById(1);
        verify(repository).delete(sampleBlotter);
    }

    @Test
    void delete_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.delete(1, 1));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(Blotter.class));
    }

    @Test
    void delete_WhenVersionMismatch_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleBlotter));
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> service.delete(1, 2));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(Blotter.class));
    }
} 