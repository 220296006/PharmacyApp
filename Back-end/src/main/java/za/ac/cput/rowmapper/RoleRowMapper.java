package za.ac.cput.rowmapper;
import org.springframework.jdbc.core.RowMapper;
import za.ac.cput.model.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/09
 * @Time : 16:00
 **/
 public class RoleRowMapper implements RowMapper<Role> {
     @Override
     public Role mapRow(ResultSet resultSet, int rowNum) throws SQLException {
         Role role = new Role();
         role.setId(resultSet.getLong("id"));
         role.setName(resultSet.getString("name"));
         // Split the permission string into individual permissions
         String permissionString = resultSet.getString("permission");
         Set<String> permissions = new HashSet<>();
         if (permissionString != null && !permissionString.isEmpty()) {
             String[] permissionArray = permissionString.split(",");
             permissions.addAll(Arrays.asList(permissionArray));
         }
         role.setPermissions(permissions);
         return role;
     }
 }
