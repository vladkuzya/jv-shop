package mate.academy.service;

import java.util.List;

public interface GenericService<T, K> {
    T create(T item);

    T getById(K id);

    List<T> getAll();

    T update(T item);

    boolean delete(K id);
}
