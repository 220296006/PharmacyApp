package com.pharmacyapp.query;

import org.springframework.security.core.parameters.P;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 17:07
 **/
public class RoleQuery {

     public static final String SELECT_ROLE_BY_NAME_QUERY = "SELECT * FROM Roles WHERE name = :name";
     public static final String INSERT_ROLE_TO_USER_QUERY = "INSERT INTO UserRoles (user_id, role_id)" +
             " VALUES (:userId, :roleId)";

     public static final String SELECT_ROLES_QUERY = "SELECT * FROM Roles";
     public static final String SELECT_ROLE_BY_ID_QUERY = "SELECT * FROM Roles WHERE id = :roleId";
     public static final String UPDATE_ROLE_QUERY = "UPDATE Roles SET name = :name, permission = :permission WHERE " +
             "id = :roleId";
     public static final String UPDATE_USER_ROLE_PERMISSIONS_QUERY = "UPDATE Roles SET permission = :permission" +
             " WHERE id = :roleId";
     public static final String DELETE_ROLE_QUERY = "DELETE FROM Roles WHERE id = :roleId";
     public static final String SELECT_ROLES_BY_USER_ID_QUERY = "SELECT r.* FROM Roles r INNER JOIN UserRoles ur ON " +
             "r.id = ur.role_id WHERE ur.user_id = :userId";
     public static final String SELECT_ROLE_BY_USER_EMAIL_QUERY = "SELECT r.* FROM Roles r INNER JOIN UserRoles ur " +
             "ON r.id = ur.role_id INNER JOIN Users u ON ur.user_id = u.id WHERE u.email = :email";
     public static final String UPDATE_USER_ROLE_QUERY = "UPDATE UserRoles SET role_id = :roleId " +
             "WHERE user_id = :userId";

}