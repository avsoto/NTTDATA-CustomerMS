package com.bankingSystem.customer_ms.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, ID> {

    void create(T t);

    void update(ID id, T t);

    void delete(ID id);

    Optional<T> getById(ID id);

    List<T> getAll();

}
