package za.ac.cput.service;

public interface IService<T, ID> {

    T save(T t);

    T read(ID id);

    T update(T t);

    boolean delete(ID id);
}
