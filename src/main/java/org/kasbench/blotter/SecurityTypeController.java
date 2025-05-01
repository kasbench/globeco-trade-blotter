package org.kasbench.blotter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/securityType")
public class SecurityTypeController {
    
    private final SecurityTypeService service;

    @Autowired
    public SecurityTypeController(SecurityTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<SecurityType> getAllSecurityTypes() {
        return service.findAll();
    }

    @GetMapping("/{securityTypeId}")
    public ResponseEntity<SecurityType> getSecurityTypeById(@PathVariable Integer securityTypeId) {
        return service.findById(securityTypeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SecurityType createSecurityType(@RequestBody SecurityType securityType) {
        return service.save(securityType);
    }

    @PutMapping("/{securityTypeId}")
    public SecurityType updateSecurityType(
            @PathVariable Integer securityTypeId,
            @RequestBody SecurityType securityType) {
        return service.update(securityTypeId, securityType);
    }

    @DeleteMapping("/{securityTypeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSecurityType(
            @PathVariable Integer securityTypeId,
            @RequestParam Integer versionId) {
        service.delete(securityTypeId, versionId);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleOptimisticLock(ObjectOptimisticLockingFailureException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("The resource was modified by another user. Please try again.");
    }
} 