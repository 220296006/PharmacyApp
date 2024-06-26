package com.pharmacyapp.service.implementation;

import com.pharmacyapp.model.Role;
import com.pharmacyapp.repository.implementation.RoleRepositoryImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.pharmacyapp.service.RoleService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImp implements RoleService {
    private final RoleRepositoryImp roleRepository;

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Collection<Role> list(String name, int page, int pageSize) {
        return roleRepository.list("roles", 5, 5);
    }

    @Override
    public Role read(Long id) {
        return roleRepository.read(id);
    }

    @Override
    public Role update(Role role) {
        return roleRepository.update(role);
    }

    @Override
    public boolean delete(Long id) {
        return roleRepository.delete(id);
    }

    @Override
    public void addRoleToUser(Long userId, String roleName, Set<String> permissions) {
        roleRepository.addRoleToUser(userId, roleName, permissions);
    }

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        return roleRepository.getRolesByUserId(userId);
    }

    @Override
    public Role getRoleByUserEmail(String email) {
        return roleRepository.getRoleByUserEmail(email);
    }

    @Override
    public Role updateUserRole(Long userId, String roleName) {
        return roleRepository.updateUserRole(userId, roleName);
    }

    @Override
    public Role findRoleByName(String roleName) {
        return roleRepository.findRoleByName(roleName);
    }

    @Override
    public Long findUserIdByEmail(String email) {
        return roleRepository.findUserIdByEmail(email);
    }

    @Override
    public Long findRoleIdByName(String roleName) {
        return roleRepository.findRoleIdByName(roleName);
    }

    @Override
    public void assignRolesToUser(String email) {
        roleRepository.assignRolesToUser(email);
    }
}
