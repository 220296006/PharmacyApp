package za.ac.cput.repository;

import za.ac.cput.model.Event;

import java.util.List;
import java.util.Optional;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/22
 * @Time : 00:07
 **/
public interface EventRepository <T extends Event>{
    List<T> findAll();
    T findById(Long id);
    T save(T t);
    T update(Event t);
    void delete(Long id);

}


