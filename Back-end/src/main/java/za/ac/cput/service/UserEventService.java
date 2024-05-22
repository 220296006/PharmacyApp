package za.ac.cput.service;

import za.ac.cput.model.UserEvent;

import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/22
 * @Time : 01:13
 **/
public interface UserEventService {
    UserEvent save(UserEvent userEvent);
    UserEvent update(UserEvent userEvent);
    boolean delete(Long id);
    UserEvent read(Long id);
    List<UserEvent> getAll();
    List<UserEvent> findByUserId(Long userId);
}
