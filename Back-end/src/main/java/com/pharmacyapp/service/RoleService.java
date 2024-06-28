package com.pharmacyapp.service;

import com.pharmacyapp.model.Role;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface RoleService {
    Role save(Role role);
    Collection<Role> list(String name, int page, int pageSize);
    Role read(Long id);
    Role update(Role role);
    boolean delete(Long id);
    void addRoleToUser(Long userId, String roleName,Set<String> permissions);
    List<Role> getRolesByUserId(Long userId);
    Role getRoleByUserEmail(String email);
    Role updateUserRole(Long userId, String roleName);
    Role findRoleByName(String roleName);
    Long findUserIdByEmail(String email);
    Long findRoleIdByName(String roleName);
    void assignRolesToUser(String email);
}
