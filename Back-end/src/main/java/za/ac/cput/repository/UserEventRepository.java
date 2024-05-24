package za.ac.cput.repository;

import za.ac.cput.model.UserEvent;

import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/22
 * @Time : 00:07
 **/
public interface UserEventRepository <T extends UserEvent> {
    List<T> findAll();
    List<T> findByUserId(Long userId);
    T save(T t);
    T read(Long id);
    T update(UserEvent t);
    void delete(Long id);

}
