package za.ac.cput.query;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/11
 * @Time : 20:17
 **/
public class PrescriptionQuery {
    public static final String INSERT_PRESCRIPTION_QUERY = "INSERT INTO Prescriptions (customer_id, doctor_name, doctor_address, issue_date) VALUES (:customerId, :doctorName, :doctorAddress, :issueDate)";
    public static final String SELECT_PRESCRIPTION_BY_ID_QUERY = "SELECT * FROM Prescriptions WHERE id = :id";
    public static final String UPDATE_PRESCRIPTION_QUERY = "UPDATE Prescriptions SET customer_id = :customerId, doctor_name = :doctorName, doctor_address = :doctorAddress, issue_date = :issueDate WHERE id = :id";
    public static final String DELETE_PRESCRIPTION_QUERY = "DELETE FROM Prescriptions WHERE id = :id";
    public static final String UPDATE_PRESCRIPTION_QUERY_LINKED_TO_CUSTOMER =  "UPDATE Prescriptions SET customer_id = :customerId WHERE id = :id";;
   public static final String FETCH_ALL_PRESCRIPTIONS_QUERY =  "SELECT * FROM Prescriptions LIMIT :size OFFSET :page";
}
