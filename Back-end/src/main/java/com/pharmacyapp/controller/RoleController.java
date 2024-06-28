package com.pharmacyapp.controller;

import com.pharmacyapp.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.pharmacyapp.service.RoleService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/roles")
@ComponentScan
@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        log.info("Creating a role: {}", role);
        Role createdRole = roleService.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @GetMapping("/list")
    public ResponseEntity<Collection<Role>> listRoles(@RequestParam Optional<String> name, @RequestParam
    Optional<Integer> page, @RequestParam Optional<Integer> pageSize) {
        log.info("Fetching roles for name {} page {} of size {}", name, page, pageSize);
        Collection<Role> roles = roleService.list(name.orElse(""), page.orElse(0),
                pageSize.orElse(10));
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Role> findRoleById(@PathVariable Long id) {
        log.info("Fetching role by ID: {}", id);
        Role role = roleService.read(id);
        if (role != null) {
            return ResponseEntity.ok(role);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        log.info("Updating role: {}", role);
        role.setId(id);
        Role updatedRole = roleService.update(role);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        log.info("Deleting role with ID: {}", id);
        boolean deleted = roleService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/addRoleToUser/{userId}")
    public ResponseEntity<Void> addRoleToUser(@RequestParam Long userId, @RequestParam String roleName,
                                              @RequestParam Set<String> permissions)  {
        log.info("Adding role {} to user with ID: {}", roleName, userId);
        roleService.addRoleToUser(userId, roleName,permissions);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getRolesByUserId/{userId}")
    public ResponseEntity<List<Role>> getRolesByUserId(@PathVariable Long userId) {
        log.info("Fetching roles by user ID: {}", userId);
        List<Role> roles = roleService.getRolesByUserId(userId);
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/getRoleByUserEmail")
    public ResponseEntity<Role> getRoleByUserEmail(@RequestParam String email) {
        log.info("Fetching role by user email: {}", email);
        Role role = roleService.getRoleByUserEmail(email);
        return ResponseEntity.ok(role);
    }

    @PutMapping("/updateUserRole/{userId}")
    public ResponseEntity<?> updateUserRole(@RequestParam Long userId, @RequestParam String roleName) {
        log.info("Updating role of user with ID: {} to role: {}", userId, roleName);
        Role updatedRole = roleService.updateUserRole(userId, roleName);
        return ResponseEntity.ok(updatedRole);
    }
}
