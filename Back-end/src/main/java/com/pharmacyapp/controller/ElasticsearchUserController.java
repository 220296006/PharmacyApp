package com.pharmacyapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.pharmacyapp.dto.UserDTO;
import com.pharmacyapp.model.User;
import com.pharmacyapp.service.UserService;
import com.pharmacyapp.service.implementation.UserServiceImp;

import java.util.List;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/06/13
 * @Time : 17:41
 **/
@RestController
@RequestMapping("/elasticsearch/users")
@RequiredArgsConstructor
public class ElasticsearchUserController {
    private final UserServiceImp userService;

    @PostMapping
    public UserDTO createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/search/firstname")
    public List<User> getUsersByFirstName(@RequestParam String firstName) {
        return userService.searchByFirstName(firstName);
    }

    @GetMapping("/search/middlename")
    public List<User> getUsersByMiddleName(@RequestParam String middleName) {
        return userService.searchByMiddleName(middleName);
    }

    @GetMapping("/search/lastname")
    public List<User> getUsersByLastName(@RequestParam String lastName) {
        return userService.searchByLastName(lastName);
    }

    @GetMapping("/search/email")
    public List<User> getUsersByEmail(@RequestParam String email) {
        return userService.searchByEmail(email);
    }

    @GetMapping("/search/address")
    public List<User> getUsersByAddress(@RequestParam String address) {
        return userService.searchByAddress(address);
    }

    @GetMapping("/search/phone")
    public List<User> getUsersByPhone (@RequestParam String phone) {
        return userService.searchByPhone(phone);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
