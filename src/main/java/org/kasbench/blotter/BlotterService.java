package org.kasbench.blotter;

import java.util.List;
import java.util.Optional;

public interface BlotterService {
    List<Blotter> findAll();
    Optional<Blotter> findById(Integer id);
    Blotter save(Blotter blotter);
    Blotter update(Integer id, Blotter blotter);
    void delete(Integer id, Integer version);
} 