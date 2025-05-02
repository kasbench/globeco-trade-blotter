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
class SecurityServiceImplTest {

    @Mock
    private SecurityRepository repository;

    private SecurityServiceImpl service;
    private Security sampleSecurity;
    private SecurityType sampleSecurityType;

    @BeforeEach
    void setUp() {
        service = new SecurityServiceImpl(repository);
        sampleSecurityType = new SecurityType(1, "STK", "Stock", 1);
        sampleSecurity = new Security(1, "IBM", "IBM Corporation", sampleSecurityType, 1);
    }

    @Test
    void findAll_ReturnsAllSecurities() {
        when(repository.findAll()).thenReturn(List.of(sampleSecurity));
        List<Security> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(sampleSecurity, result.get(0));
        verify(repository).findAll();
    }

    @Test
    void findById_WhenExists_ReturnsSecurity() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleSecurity));
        Optional<Security> result = service.findById(1);
        assertTrue(result.isPresent());
        assertEquals(sampleSecurity, result.get());
        verify(repository).findById(1);
    }

    @Test
    void findById_WhenNotExists_ReturnsEmpty() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        Optional<Security> result = service.findById(1);
        assertFalse(result.isPresent());
        verify(repository).findById(1);
    }

    @Test
    void save_SavesSecurity() {
        when(repository.save(sampleSecurity)).thenReturn(sampleSecurity);
        Security result = service.save(sampleSecurity);
        assertEquals(sampleSecurity, result);
        verify(repository).save(sampleSecurity);
    }

    @Test
    void update_WhenExists_UpdatesSecurity() {
        SecurityType newType = new SecurityType(2, "OPT", "Option", 1);
        Security updated = new Security(1, "AAPL", "Apple Inc.", newType, 1);
        when(repository.findById(1)).thenReturn(Optional.of(sampleSecurity));
        when(repository.save(any(Security.class))).thenReturn(updated);
        Security result = service.update(1, updated);
        assertEquals("AAPL", result.getTicker());
        assertEquals("Apple Inc.", result.getDescription());
        assertEquals(newType, result.getSecurityType());
        verify(repository).findById(1);
        verify(repository).save(any(Security.class));
    }

    @Test
    void update_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.update(1, sampleSecurity));
        verify(repository).findById(1);
        verify(repository, never()).save(any(Security.class));
    }

    @Test
    void delete_WhenExistsAndVersionMatches_DeletesSecurity() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleSecurity));
        service.delete(1, 1);
        verify(repository).findById(1);
        verify(repository).delete(sampleSecurity);
    }

    @Test
    void delete_WhenNotExists_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.delete(1, 1));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(Security.class));
    }

    @Test
    void delete_WhenVersionMismatch_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.of(sampleSecurity));
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> service.delete(1, 2));
        verify(repository).findById(1);
        verify(repository, never()).delete(any(Security.class));
    }
} 