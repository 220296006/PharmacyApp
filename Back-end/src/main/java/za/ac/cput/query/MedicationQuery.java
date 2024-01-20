package za.ac.cput.query;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/12
 * @Time : 00:54
 **/
public class MedicationQuery {
    public static final String INSERT_MEDICATION_QUERY = "INSERT INTO Medications (prescription_id, name, dosage, frequency) VALUES (:prescription_id, :name, :dosage, :frequency)";
    public static final String SELECT_MEDICATION_BY_ID_QUERY = "SELECT * FROM Medications WHERE id = :id";
    public static  final String SELECT_MEDICATION_BY_PRESCRIPTION_ID_QUERY = "SELECT * FROM Medications WHERE prescription_id = :prescription_id";

    public static final String UPDATE_MEDICATION_QUERY = "UPDATE Medications SET prescription_id = :prescription_id, name = :name, dosage = :dosage, frequency = :frequency WHERE id = :id";
    public static final String DELETE_MEDICATION_QUERY = "DELETE FROM Medications WHERE id = :id";
    public static final String UPDATE_PRESCRIPTION_LINKED_TO_MEDICATION_QUERY = "UPDATE Medications SET prescription_id = :prescription_id WHERE id = :id";
    public static final String FETCH_ALL_MEDICATIONS_QUERY = "SELECT * FROM Medications LIMIT :size OFFSET :page";

}

