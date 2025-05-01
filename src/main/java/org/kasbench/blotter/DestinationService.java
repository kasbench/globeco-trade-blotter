package org.kasbench.blotter;

import java.util.List;
import java.util.Optional;

public interface DestinationService {
    List<Destination> findAll();
    Optional<Destination> findById(Integer id);
    Destination save(Destination destination);
    Destination update(Integer id, Destination destination);
    void delete(Integer id, Integer version);
} 