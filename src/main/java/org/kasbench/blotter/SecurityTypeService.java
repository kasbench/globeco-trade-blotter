package org.kasbench.blotter;

import java.util.List;
import java.util.Optional;

public interface SecurityTypeService {
    List<SecurityType> findAll();
    Optional<SecurityType> findById(Integer id);
    SecurityType save(SecurityType securityType);
    SecurityType update(Integer id, SecurityType securityType);
    void delete(Integer id, Integer version);
} 