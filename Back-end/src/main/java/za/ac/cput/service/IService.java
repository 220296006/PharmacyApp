package za.ac.cput.service;

import java.util.Collection;

public interface IService<T, ID> {

    T save(T t);
    Collection<T> list(int page, int pageSize);

    T read(ID id);

    T update(T t);

    boolean delete(ID id);

}
