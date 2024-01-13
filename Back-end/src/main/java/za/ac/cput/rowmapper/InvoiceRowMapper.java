package za.ac.cput.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import za.ac.cput.model.Customer;
import za.ac.cput.model.Invoice;

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
        invoice.setAmount(rs.getString("amount"));
        invoice.setDueDate(rs.getDate("due_date"));
        invoice.setPaid(rs.getBoolean("paid"));
        Customer customer = new Customer();
        customer.setId(rs.getLong("customer_id"));
        invoice.setCustomer(customer);
        return invoice;
    }
}
