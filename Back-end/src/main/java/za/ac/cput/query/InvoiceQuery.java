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
    public static final String SELECT_INVOICE_BY_CUSTOMER_ID_QUERY = "SELECT * FROM Invoices WHERE customer_id = :customerId";

    public static final String SELECT_INVOICE_COUNT_QUERY = "SELECT COUNT(*) as invoiceCount FROM Invoices;\n";

    public static final String SELECT_TOTAL_BILLED_AMOUNT_QUERY = "SELECT SUM(amount) as totalBilledAmount FROM Invoices;\n";
    public static final String UPDATE_INVOICE_QUERY = "UPDATE Invoices SET customer_id = :customerId, amount = :amount, due_date = :dueDate, payment_status = :paymentStatus WHERE id = :id";
    public static final String DELETE_INVOICE_QUERY = "DELETE FROM Invoices WHERE id = :id";
    public static final String FETCH_ALL_INVOICES_QUERY = "SELECT * FROM Invoices LIMIT :size OFFSET :page";

    public static final String UPDATE_CUSTOMER_LINKED_TO_INVOICE_QUERY = "UPDATE Invoices SET customer_id = :customerId WHERE id = :id";

}

