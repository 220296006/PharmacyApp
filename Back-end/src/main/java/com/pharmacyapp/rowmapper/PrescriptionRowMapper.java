package com.pharmacyapp.rowmapper;

import com.pharmacyapp.model.Customer;
import com.pharmacyapp.model.Prescription;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
@author : Thabiso Matsaba
@Project : PharmacyApp
@Date : 2023/07/11
@Time : 20:40
**/
public class PrescriptionRowMapper implements RowMapper<Prescription> {

    @Override
    public Prescription mapRow(ResultSet rs, int rowNum) throws SQLException {
        Prescription prescription = new Prescription();
        prescription.setId(rs.getLong("id"));
        prescription.setDoctorName(rs.getString("doctor_name"));
        prescription.setDoctorAddress(rs.getString("doctor_address"));
        prescription.setIssueDate(rs.getDate("issue_date"));

        Customer customer = new Customer();
        customer.setId(rs.getLong("customer_id"));
        prescription.setCustomer(customer);
        return prescription;
    }
}
