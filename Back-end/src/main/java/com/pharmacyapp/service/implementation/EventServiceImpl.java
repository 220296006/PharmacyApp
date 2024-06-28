package com.pharmacyapp.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.pharmacyapp.model.Event;
import com.pharmacyapp.repository.EventRepository;
import com.pharmacyapp.service.EventService;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/22
 * @Time : 01:04
 **/
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventServiceImpl implements EventService {

private final EventRepository <Event> eventRepository;

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event update(Event event) {
        return eventRepository.update(event);
    }

    @Override
    public  boolean delete(Long id) {
    eventRepository.delete(id);
    return true;
    }

    @Override
    public Event read(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public Collection<Event> getAll() {
        return List.of((Event) eventRepository.findAll());
    }
}
