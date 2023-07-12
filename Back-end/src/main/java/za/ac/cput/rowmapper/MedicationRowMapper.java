package za.ac.cput.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import za.ac.cput.model.Medication;
import za.ac.cput.model.Prescription;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/12
 * @Time : 01:11
 **/
public class MedicationRowMapper implements RowMapper<Medication> {
    @Override
    public Medication mapRow(ResultSet rs, int rowNum) throws SQLException {
       Medication medication = new Medication();
       medication.setId(rs.getLong("id"));
       medication.setName(rs.getString("name"));
       medication.setDosage(rs.getString("dosage"));
       medication.setFrequency(rs.getString("frequency"));

       Prescription prescription = new Prescription();
       prescription.setId(rs.getLong("prescription_id"));

       return medication;
    }

}
