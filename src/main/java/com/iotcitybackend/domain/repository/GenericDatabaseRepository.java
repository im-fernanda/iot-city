package com.iotcitybackend.domain.repository;

import java.util.List;
import java.util.Optional;

public interface GenericDatabaseRepository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
    boolean existsById(ID id);
    long count();
} 