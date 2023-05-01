package za.ac.cput.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.util.Objects;
import static jakarta.persistence.GenerationType.AUTO;

@Entity
@AllArgsConstructor
public class Inventory implements Serializable {

    @Id
    @GeneratedValue(strategy = AUTO, generator="system-uuid")
    private Long inventoryID;
    private String tabletStockAmount;
    private String medicineStockAmount;


    protected Inventory(){}

    private Inventory(Builder builder) {
        this.inventoryID = builder.inventoryID;
        this.tabletStockAmount = builder.tabletStockAmount;
        this.medicineStockAmount = builder.medicineStockAmount;
    }

    public Long getInventoryID() {
        return inventoryID;
    }

    public String getTabletStockAmount() {
        return tabletStockAmount;
    }

    public String getMedicineStockAmount() {
        return medicineStockAmount;
    }


    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryID=" + inventoryID +
                ", tabletStockAmount=" + tabletStockAmount +
                ", medicineStockAmount=" + medicineStockAmount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventory inventory)) return false;
        return Objects.equals(getInventoryID(), inventory.getInventoryID()) && Objects.equals(getTabletStockAmount(), inventory.getTabletStockAmount()) && Objects.equals(getMedicineStockAmount(), inventory.getMedicineStockAmount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInventoryID(), getTabletStockAmount(), getMedicineStockAmount());
    }

    public static class Builder{
        private Long inventoryID;
                String tabletStockAmount, medicineStockAmount;

        public Builder setInventoryID(Long inventoryID){
            this.inventoryID = inventoryID;
            return this;
        }

        public Builder setTabletStockAmount(String tabletStockAmount){
            this.tabletStockAmount = tabletStockAmount;
            return this;
        }

        public Builder setMedicineStockAmount(String medicineStockAmount){
            this.medicineStockAmount = medicineStockAmount;
            return this;
        }


        public Inventory build(){
            return new Inventory(this);
        }
    }}

