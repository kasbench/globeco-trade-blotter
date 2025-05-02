package org.kasbench.blotter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/blotter")
public class BlotterController {
    private final BlotterService service;

    @Autowired
    public BlotterController(BlotterService service) {
        this.service = service;
    }

    @GetMapping
    public List<Blotter> getAllBlotters() {
        return service.findAll();
    }

    @GetMapping("/{blotterId}")
    public ResponseEntity<Blotter> getBlotterById(@PathVariable Integer blotterId) {
        return service.findById(blotterId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Blotter createBlotter(@RequestBody Blotter blotter) {
        return service.save(blotter);
    }

    @PutMapping("/{blotterId}")
    public Blotter updateBlotter(
            @PathVariable Integer blotterId,
            @RequestBody Blotter blotter) {
        return service.update(blotterId, blotter);
    }

    @DeleteMapping("/{blotterId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBlotter(
            @PathVariable Integer blotterId,
            @RequestParam Integer versionId) {
        service.delete(blotterId, versionId);
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