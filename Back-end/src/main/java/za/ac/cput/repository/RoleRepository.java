package za.ac.cput.repository;
import za.ac.cput.model.Role;
import java.util.Collection;

public interface RoleRepository <T extends Role> {
        T save(T t);
        Collection<T> list(int page, int pageSize);
        T read(Long id);
        T update(T t);
        boolean delete(Long id);
        void addRoleToUser(Long userId, String roleName);
        Role getRoleByUserId(Long userId);
        Role getRoleByUserEmail(String email);
        Role updateUserRole(Long userId , String roleName);
}
