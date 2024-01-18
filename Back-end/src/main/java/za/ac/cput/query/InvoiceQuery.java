package za.ac.cput.query;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/12
 * @Time : 03:48
 **/
public class InvoiceQuery {
    public static final String INSERT_INVOICE_QUERY = "INSERT INTO Invoices (customer_id, amount, due_date, payment_status) VALUES (:customerId, :amount, :dueDate, :paymentStatus)";
    public static final String SELECT_INVOICE_BY_ID_QUERY = "SELECT * FROM Invoices WHERE id = :id";
    public static final String UPDATE_INVOICE_QUERY = "UPDATE Invoices SET customer_id = :customerId, amount = :amount, due_date = :dueDate, payment_status = :paymentStatus WHERE id = :id";
    public static final String DELETE_INVOICE_QUERY = "DELETE FROM Invoices WHERE id = :id";
    public static final String FETCH_ALL_INVOICES_QUERY = "SELECT * FROM Invoices LIMIT :size OFFSET :page";

    public static final String UPDATE_CUSTOMER_LINKED_TO_INVOICE_QUERY = "UPDATE Invoices SET customer_id = :customer_id WHERE id = :id";

}

