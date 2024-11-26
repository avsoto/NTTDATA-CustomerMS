package com.bankingSystem.customer_ms.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, I> {

    T create(T t);

    void update(I id, T t);

    void delete(I id);

    Optional<T> getById(I id);

    List<T> getAll();

}
