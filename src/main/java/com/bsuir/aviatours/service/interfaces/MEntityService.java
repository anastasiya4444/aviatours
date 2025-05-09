package com.bsuir.aviatours.service.interfaces;

public interface MEntityService<T, T1> extends EntityService<T> {
    T findEntityById(T1 id);
}
