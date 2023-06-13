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
public class Pharmacy implements Serializable {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long pharmacyId;
    private String pharmacyName;
    private String medicationId;
    private String inventoryId;

    protected Pharmacy() {}

    private Pharmacy(Builder builder) {
        this.pharmacyId = builder.pharmacyId;
        this.pharmacyName = builder.pharmacyName;
        this.medicationId = builder.medicationId;
        this.inventoryId = builder.inventoryId;
    }

    public Long getPharmacyId() {
        return pharmacyId;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    @Override
    public String toString() {
        return "Pharmacy{" +
                "pharmacyId='" + pharmacyId + '\'' +
                ", pharmacyName='" + pharmacyName + '\'' +
                ", medicationId='" + medicationId + '\'' +
                ", inventoryId='" + inventoryId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pharmacy pharmacy)) return false;
        return Objects.equals(getPharmacyId(), pharmacy.getPharmacyId()) && Objects.equals(getPharmacyName(), pharmacy.getPharmacyName()) && Objects.equals(getMedicationId(), pharmacy.getMedicationId()) && Objects.equals(getInventoryId(), pharmacy.getInventoryId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPharmacyId(), getPharmacyName(), getMedicationId(), getInventoryId());
    }

    public static class Builder{
        private Long pharmacyId;
                String  pharmacyName,medicationId, inventoryId ;

        public Builder setPharmacyId(Long pharmacyId){
            this.pharmacyId = pharmacyId;
            return this;
        }

        public Builder setPharmacyName(String pharmacyName){
            this.pharmacyName = pharmacyName;
            return this;
        }

        public Builder setMedicationId(String medicationId){
            this.medicationId = medicationId;
            return this;
        }

        public Builder setInventoryId(String inventoryId){
            this.inventoryId = inventoryId;
            return this;
        }

        public Pharmacy build(){
            return new Pharmacy(this);
        }
    }
}
