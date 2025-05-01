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
class SecurityTypeServiceImplTest {

    @Mock
    private SecurityTypeRepository repository;

    private SecurityTypeServiceImpl service;
    private SecurityType sampleSecurityType;

    @BeforeEach
    void setUp() {
        service = new SecurityTypeServiceImpl(repository);
        sampleSecurityType = new SecurityType(1, "CS", "Common Stock", 1);
    }

    @Test
    void findAll_ReturnsAllSecurityTypes() {
        when(repository.findAll()).thenReturn(List.of(sampleSecurityType));

        List<SecurityType> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals(sampleSecurityType, result.get(0));
        verify(repository).findAll();
    }

    @Test
    void findById_WhenExists_ReturnsSecurityType() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleSecurityType));

        Optional<SecurityType> result = service.findById(1);

        assertTrue(result.isPresent());
        assertEquals(sampleSecurityType, result.get());
        verify(repository).findById(1);
    }

    @Test
    void findById_WhenNotExists_ReturnsEmpty() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        Optional<SecurityType> result = service.findById(1);

        assertFalse(result.isPresent());
        verify(repository).findById(1);
    }

    @Test
    void save_SavesSecurityType() {
        when(repository.save(sampleSecurityType)).thenReturn(sampleSecurityType);

        SecurityType result = service.save(sampleSecurityType);

        assertEquals(sampleSecurityType, result);
        verify(repository).save(sampleSecurityType);
    }

    @Test
    void update_WhenExists_UpdatesSecurityType() {
        SecurityType updatedType = new SecurityType(1, "PS", "Preferred Stock", 1);
        when(repository.findById(1)).thenReturn(Optional.of(sampleSecurityType));
        when(repository.save(any(SecurityType.class))).thenReturn(updatedType);

        SecurityType result = service.update(1, updatedType);

        assertEquals("PS", result.getAbbreviation());
        assertEquals("Preferred Stock", result.getDescription());
        verify(repository).findById(1);
        verify(repository).save(any(SecurityType.class));
    }

    @Test
    void update_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.update(1, sampleSecurityType));
        verify(repository).findById(1);
        verify(repository, never()).save(any(SecurityType.class));
    }

    @Test
    void delete_WhenExistsAndVersionMatches_DeletesSecurityType() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleSecurityType));

        service.delete(1, 1);

        verify(repository).findById(1);
        verify(repository).delete(sampleSecurityType);
    }

    @Test
    void delete_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.delete(1, 1));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(SecurityType.class));
    }

    @Test
    void delete_WhenVersionMismatch_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleSecurityType));

        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> service.delete(1, 2));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(SecurityType.class));
    }
} 