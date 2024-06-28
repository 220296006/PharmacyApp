package com.pharmacyapp.rowmapper;

import com.pharmacyapp.enumeration.PaymentStatus;
import com.pharmacyapp.model.Customer;
import com.pharmacyapp.model.Invoice;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/12
 * @Time : 17:35
 **/
public class InvoiceRowMapper implements RowMapper<Invoice> {
  @Override
    public Invoice mapRow(ResultSet rs, int rowNum) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getLong("id"));
        invoice.setAmount(BigInteger.valueOf(rs.getInt("amount")));
        invoice.setDueDate(rs.getDate("due_date"));
        invoice.setPaymentStatus(PaymentStatus.valueOf(rs.getString("payment_status")));

        Customer customer = new Customer();
        customer.setId(rs.getLong("customer_id"));
        invoice.setCustomer(customer);
        return invoice;
    }
}
