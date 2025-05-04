package org.kasbench.blotter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/security")
public class SecurityController {
    private final SecurityService service;
    private final SecurityTypeRepository securityTypeRepository;

    @Autowired
    public SecurityController(SecurityService service, SecurityTypeRepository securityTypeRepository) {
        this.service = service;
        this.securityTypeRepository = securityTypeRepository;
    }

    @GetMapping
    public List<Security> getAllSecurities() {
        return service.findAll();
    }

    @GetMapping("/{securityId}")
    public ResponseEntity<Security> getSecurityById(@PathVariable Integer securityId) {
        return service.findById(securityId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Security createSecurity(@RequestBody SecurityRequestDTO dto) {
        SecurityType securityType = securityTypeRepository.findById(dto.getSecurityTypeId())
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("SecurityType not found with id: " + dto.getSecurityTypeId()));
        Security security = new Security();
        security.setTicker(dto.getTicker());
        security.setDescription(dto.getDescription());
        security.setVersion(dto.getVersion());
        security.setSecurityType(securityType);
        return service.save(security);
    }

    @PutMapping("/{securityId}")
    public Security updateSecurity(
            @PathVariable Integer securityId,
            @RequestBody Security security) {
        return service.update(securityId, security);
    }

    @DeleteMapping("/{securityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSecurity(
            @PathVariable Integer securityId,
            @RequestParam Integer versionId) {
        service.delete(securityId, versionId);
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