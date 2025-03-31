package com.bsuir.aviatours.service.interfaces;

import java.util.List;

public interface EntityService<T> {
    T saveEntity(T obj);
    List<T> getAllEntities();
    T updateEntity(T obj);
    void deleteEntity(T obj);
    T findEntityById(int id);
}
