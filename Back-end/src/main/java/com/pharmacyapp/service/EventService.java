package com.pharmacyapp.service;

import com.pharmacyapp.model.Event;

import java.util.Collection;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/22
 * @Time : 01:02
 **/
public interface EventService {
    Event save(Event event);

    Event update(Event event);

    boolean delete(Long  id);

    Event read(Long id);

    Collection<Event> getAll();
}
