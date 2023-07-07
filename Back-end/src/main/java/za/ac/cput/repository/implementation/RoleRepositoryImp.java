package za.ac.cput.repository.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.Role;
import za.ac.cput.repository.RoleRepository;
import za.ac.cput.rowmapper.RoleRowMapper;

import java.util.Collection;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static za.ac.cput.enumeration.RoleType.ROLE_USER;
import static za.ac.cput.query.RoleQuery.*;

@RequiredArgsConstructor
@Repository
@Slf4j
public class RoleRepositoryImp implements RoleRepository<Role> {
        private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Role save(Role role) {
        return null;
    }

    @Override
    public Collection<Role> list(int page, int pageSize) {
        return null;
    }

    @Override
    public Role read(Long id) {
        return null;
    }

    @Override
    public Role update(Role role) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) {
        log.info("Adding  role {} to user id: {}", roleName, userId);
      try{
         Role role = jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY, Map.of("name", roleName), new RoleRowMapper());
         jdbc.update(INSERT_ROLE_TO_USER_QUERY, Map.of("userId", userId, "roleId", requireNonNull(role).getId()));
        } catch (EmptyResultDataAccessException exception){
              throw new ApiException("No role found by name" + ROLE_USER.name());
        } catch (Exception exception){
          log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");

        }
    }

    @Override
    public Role getRoleByUserId(Long userId) {
        return null;
    }

    @Override
    public Role getRoleByUserEmail(String email) {
        return null;
    }

    @Override
    public Role updateUserRole(Long userId, String roleName) {
        return null;
    }
}
