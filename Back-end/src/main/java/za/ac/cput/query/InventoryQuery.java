package za.ac.cput.query;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 00:37
 **/
public class InventoryQuery {
    public static final String INSERT_INVENTORY_QUERY = "INSERT INTO Inventory (name, description, quantity, price, medication_id) VALUES (:name, :description, :quantity, :price, :medication_id)";
    public static final String SELECT_ALL_INVENTORY_QUERY = "SELECT * FROM Inventory LIMIT :size OFFSET :page";
    public static final String SELECT_INVENTORY_BY_ID_QUERY = "SELECT * FROM Inventory WHERE id = :id";
    public static final String UPDATE_INVENTORY_LINKED_MEDICATION = "UPDATE Inventory SET medication_id = medication_id WHERE id = :id";
    public static final String UPDATE_INVENTORY_QUERY = "UPDATE Inventory SET name = :name, description = :description, quantity = :quantity, price = :price, medication_id = :medication_id WHERE id = :id";
    public static final String DELETE_INVENTORY_QUERY = "DELETE FROM Inventory WHERE id = :id";
}
