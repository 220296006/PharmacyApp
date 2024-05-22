package za.ac.cput.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.model.UserEvent;
import za.ac.cput.service.UserEventService;

import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/22
 * @Time : 01:22
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/user-events")
@ComponentScan
public class UserEventController {

    private final UserEventService userEventService;

    @GetMapping("/all")
    public List<UserEvent> getAllUserEvents() {
        return userEventService.getAll();
    }

    @GetMapping("/read/{id}")
    public UserEvent getUserEventById(@PathVariable Long id) {
        return userEventService.read(id);
    }

    @GetMapping("/user/{userId}")
    public List<UserEvent> getUserEventsByUserId(@PathVariable Long userId) {
        return userEventService.findByUserId(userId);
    }

    @PostMapping("/create")
    public UserEvent createUserEvent(@RequestBody UserEvent userEvent) {
        return userEventService.save(userEvent);
    }

    @PutMapping("/update/{id}")
    public UserEvent updateUserEvent(@PathVariable Long id, @RequestBody UserEvent userEvent) {
        userEvent.setId(id);
        return userEventService.update(userEvent);
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteUserEvent(@PathVariable Long id) {
        return userEventService.delete(id);
    }
}
