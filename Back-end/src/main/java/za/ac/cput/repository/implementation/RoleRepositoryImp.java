package za.ac.cput.repository.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.Role;
import za.ac.cput.repository.RoleRepository;
import za.ac.cput.rowmapper.RoleRowMapper;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static za.ac.cput.enumeration.RoleType.ROLE_USER;
import static za.ac.cput.query.RoleQuery.*;
 /**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/06/01
 * @Time : 15:00
 **/
@RequiredArgsConstructor
@Repository
@Slf4j
public class RoleRepositoryImp implements RoleRepository<Role> {
        private final NamedParameterJdbcTemplate jdbc;

 @Override
public Role save(Role role) {
    log.info("Saving A Role {}:", role);
    try {
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", role.getName())
                .addValue("permission", role.getPermission());

        jdbc.update(INSERT_ROLE_QUERY, parameters, holder);
        role.setId(Objects.requireNonNull(holder.getKey()).longValue());

        return role;
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while saving the role. Please try again.");
    }
}

   @Override
public Collection<Role> list(int page, int pageSize) {
    log.info("Retrieving list of roles (Page: {}, PageSize: {})", page, pageSize);
    try {
        int offset = (page - 1) * pageSize;
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("offset", offset)
                .addValue("pageSize", pageSize);

        return jdbc.query(SELECT_ROLES_QUERY, parameters, new RoleRowMapper());
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while retrieving the list of roles. Please try again.");
    }
}

    @Override
public Role read(Long id) {
    log.info("Retrieving role with ID: {}", id);
    try {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbc.queryForObject(SELECT_ROLE_BY_ID_QUERY, parameters, new RoleRowMapper());
    } catch (EmptyResultDataAccessException exception) {
        throw new ApiException("Role not found with ID: " + id);
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while retrieving the role. Please try again.");
    }
}


  @Override
public Role update(Role role) {
    log.info("Updating role: {}", role);
    try {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", role.getId())
                .addValue("name", role.getName())
                .addValue("permission", role.getPermission());

        jdbc.update(UPDATE_ROLE_QUERY, parameters);
        return role;
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while updating the role. Please try again.");
    }
}

  @Override
public boolean delete(Long id) {
    log.info("Deleting role with ID: {}", id);
    try {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", id);
        int rowsAffected = jdbc.update(DELETE_ROLE_QUERY, parameters);
        return rowsAffected > 0;
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while deleting the role. Please try again.");
    }
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
public void getRoleByUserId(Long userId) {
    log.info("Retrieving role by user ID: {}", userId);
    try {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("userId", userId);
        jdbc.queryForObject(SELECT_ROLE_BY_USER_ID_QUERY, parameters, new RoleRowMapper());
    } catch (EmptyResultDataAccessException exception) {
        log.error(exception.getMessage());
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while retrieving the role. Please try again.");
    }
}


   @Override
public Role getRoleByUserEmail(String email) {
    log.info("Retrieving role by user email: {}", email);
    try {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("email", email);
        return jdbc.queryForObject(SELECT_ROLE_BY_USER_EMAIL_QUERY, parameters, new RoleRowMapper());
    } catch (EmptyResultDataAccessException exception) {
        return null; // Handle the case where no role is found for the given user email
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while retrieving the role. Please try again.");
    }
}


 @Override
public Role updateUserRole(Long userId, String roleName) {
    log.info("Updating role of user with ID: {} to role: {}", userId, roleName);
    try {
        Role role = jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY, Map.of("name", roleName), new RoleRowMapper());
        if (role == null) {
            throw new ApiException("Role not found. Please provide a valid role name.");
        }
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("roleId", role.getId());
        jdbc.update(UPDATE_USER_ROLE_QUERY, parameters);
        return role;
    } catch (EmptyResultDataAccessException exception) {
        throw new ApiException("No role found by name: " + roleName);
    } catch (Exception exception) {
        log.error(exception.getMessage());
        throw new ApiException("An error occurred while updating the user role. Please try again.");
    }
}

}
