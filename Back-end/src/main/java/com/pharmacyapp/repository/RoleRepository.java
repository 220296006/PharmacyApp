package com.pharmacyapp.repository;
import com.pharmacyapp.model.Role;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/08
 * @Time : 15:00
 **/
public interface RoleRepository <T extends Role> {
        T save(T t);
        Collection<T> list(String name, int page, int pageSize);
        T read(Long id);
        T update(T t);
        boolean delete(Long id);
        void addRoleToUser(Long userId, String roleName, Set<String> permissions);
         List<Role> getRolesByUserId(Long userId);
        Role getRoleByUserEmail(String email);
        Role updateUserRole(Long userId , String roleName);

        Role  findRoleByName(String roleName);

        Long findUserIdByEmail(String email);

        Long findRoleIdByName(String roleName);
}
