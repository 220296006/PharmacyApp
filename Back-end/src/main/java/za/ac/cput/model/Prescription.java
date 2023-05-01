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
public class Prescription implements Serializable {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long prescriptionId;
    private String prescriptionType;
    private String prescriptionDose;

    protected Prescription(){}

    //builder constructor
    private Prescription(Builder builder){
        this.prescriptionId = builder.prescriptionId;
        this.prescriptionType = builder.prescriptionType;
        this.prescriptionDose = builder.prescriptionDose;
    }

    //getters
    public Long getPrescriptionId() {
        return prescriptionId;
    }
    public String getPrescriptionType() {
        return prescriptionType;
    }
    public String getPrescriptionDose() {
        return prescriptionDose;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionId=" + prescriptionId +
                ", prescriptionType='" + prescriptionType + '\'' +
                ", prescriptionDose=" + prescriptionDose +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prescription that)) return false;
        return Objects.equals(getPrescriptionId(), that.getPrescriptionId()) && Objects.equals(getPrescriptionType(), that.getPrescriptionType()) && Objects.equals(getPrescriptionDose(), that.getPrescriptionDose());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrescriptionId(), getPrescriptionType(), getPrescriptionDose());
    }

    public static class Builder{
        private Long prescriptionId;
        private String prescriptionType;
        private String prescriptionDose;

        public Builder setPrescriptionId(Long prescriptionId) {
            this.prescriptionId = prescriptionId;
            return this;
        }
        public Builder setPrescriptionType(String prescriptionType) {
            this.prescriptionType = prescriptionType;
            return this;
        }
        public Builder setPrescriptionDose(String prescriptionDose) {
            this.prescriptionDose = prescriptionDose;
            return this;
        }

        public Builder copy(Prescription prescription){
            this.prescriptionId = prescription.prescriptionId;
            this.prescriptionType = prescription.prescriptionType;
            this.prescriptionDose = prescription.prescriptionDose;
            return this;
        }

        public Prescription build(){
            return new Prescription(this);
        }
    }
}
