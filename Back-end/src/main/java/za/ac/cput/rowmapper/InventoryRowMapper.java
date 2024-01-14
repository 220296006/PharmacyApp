package za.ac.cput.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import za.ac.cput.model.Inventory;
import za.ac.cput.model.Medication;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

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
        inventory.setQuantity(BigInteger.valueOf(rs.getInt("quantity")));
        inventory.setPrice(BigInteger.valueOf(rs.getInt("price")));
        inventory.setCreated_at(LocalDateTime.now());
        inventory.setUpdated_at(LocalDateTime.now());

        Medication medication = new Medication();
        medication.setId(rs.getLong("medication_id"));
        return inventory;
    }
}
