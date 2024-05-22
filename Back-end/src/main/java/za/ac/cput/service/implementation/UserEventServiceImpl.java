package za.ac.cput.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.ac.cput.model.UserEvent;
import za.ac.cput.repository.UserEventRepository;
import za.ac.cput.service.UserEventService;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/22
 * @Time : 01:13
 **/
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserEventServiceImpl implements UserEventService {
    private final UserEventRepository<UserEvent> userEventRepository;

    @Override
    public UserEvent save(UserEvent userEvent) {
        return userEventRepository.save(userEvent);
    }

    @Override
    public UserEvent update(UserEvent userEvent) {
        return userEventRepository.update(userEvent);
    }

    @Override
    public boolean delete(Long id) {
        userEventRepository.delete(id);
        return true;
    }

    @Override
    public UserEvent read(Long id) {
        return userEventRepository.read(id);
    }

    @Override
    public List<UserEvent> getAll() {
        return userEventRepository.findAll();
    }

    @Override
    public List<UserEvent> findByUserId(Long userId) {
        return userEventRepository.findByUserId(userId);
    }
}