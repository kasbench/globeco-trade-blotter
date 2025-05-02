package org.kasbench.blotter;

import java.util.List;
import java.util.Optional;

public interface SecurityService {
    List<Security> findAll();
    Optional<Security> findById(Integer id);
    Security save(Security security);
    Security update(Integer id, Security security);
    void delete(Integer id, Integer version);
} 