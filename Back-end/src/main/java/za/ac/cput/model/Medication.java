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
public class Medication implements Serializable {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long medicationId;
    private String medicationName;
    private String medicationManufacturer;
    private String supplierId;

    //Getter
    public Long getMedicationId() {
        return medicationId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public String getMedicationManufacturer() {
        return medicationManufacturer;
    }

    public String getSupplierId() {
        return supplierId;
    }

    @Override
    public String toString() {
        return "Medication{" +
                "medicationId='" + medicationId + '\'' +
                ", medicationName='" + medicationName + '\'' +
                ", medicationManufacturer='" + medicationManufacturer + '\'' +
                ", supplierId='" + supplierId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medication that)) return false;
        return Objects.equals(getMedicationId(), that.getMedicationId()) && Objects.equals(getMedicationName(), that.getMedicationName()) && Objects.equals(getMedicationManufacturer(), that.getMedicationManufacturer()) && Objects.equals(getSupplierId(), that.getSupplierId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMedicationId(), getMedicationName(), getMedicationManufacturer(), getSupplierId());
    }

    //Builder Constructor
    protected Medication(){}

    private  Medication (Medication.Builder builder){
        this.medicationId = builder.medicationId;
        this.medicationName = builder.medicationName;
        this.medicationManufacturer = builder.medicationManufacturer;
        this.supplierId = builder.supplierId;
    }
    //Builder Classes
    public static class Builder {
        private Long medicationId;
        private String medicationName;
        private String medicationManufacturer;
        private String supplierId;

        public Builder setMedicationId(Long medicationId) {
            this.medicationId = medicationId;
            return this;
        }

        public Builder setMedicationName(String medicationName) {
            this.medicationName = medicationName;
            return this;
        }

        public Builder setMedicationManufacturer(String medicationManufacturer) {
            this.medicationManufacturer = medicationManufacturer;
            return this;
        }

        public Medication.Builder setSupplierId(String supplierId) {
            this.supplierId = supplierId;
            return this;
        }

        public Medication.Builder copy(Medication medication)
        {
            this.medicationId = medication.medicationId;
            this.medicationName = medication.medicationName;
            this.medicationManufacturer = medication.medicationManufacturer;
            this.supplierId = medication.supplierId;
            return this;
        }
        public Medication build(){ return new Medication(this);}

    }
}