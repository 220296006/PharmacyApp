package za.ac.cput.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import za.ac.cput.model.Inventory;
import za.ac.cput.model.Medication;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 15:04
 **/
public class InventoryRowMapper implements RowMapper<Inventory> {
    @Override
       public Inventory mapRow(ResultSet rs, int rowNum) throws SQLException {
        Inventory inventory = new Inventory();
        inventory.setId(rs.getLong("id"));
        inventory.setName(rs.getString("name"));
        inventory.setDescription(rs.getString("description"));
        inventory.setQuantity(rs.getInt("quantity"));
        inventory.setPrice(rs.getBigDecimal("price"));

        Medication medication = new Medication();
        medication.setId(rs.getLong("medication_id"));
        return inventory;
    }
}
