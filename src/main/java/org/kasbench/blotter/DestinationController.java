package org.kasbench.blotter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/destination")
public class DestinationController {
    private final DestinationService service;

    @Autowired
    public DestinationController(DestinationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Destination> getAllDestinations() {
        return service.findAll();
    }

    @GetMapping("/{destinationId}")
    public ResponseEntity<Destination> getDestinationById(@PathVariable Integer destinationId) {
        return service.findById(destinationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Destination createDestination(@RequestBody Destination destination) {
        return service.save(destination);
    }

    @PutMapping("/{destinationId}")
    public Destination updateDestination(
            @PathVariable Integer destinationId,
            @RequestBody Destination destination) {
        return service.update(destinationId, destination);
    }

    @DeleteMapping("/{destinationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDestination(
            @PathVariable Integer destinationId,
            @RequestParam Integer versionId) {
        service.delete(destinationId, versionId);
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