package za.ac.cput.factory;
import za.ac.cput.model.Medication;
import za.ac.cput.utils.StringHelper;

public class MedicationFactory {

    public static Medication createMedication(Long medicationId, String medicationName, String medicationManufacturer, String supplierId){

        if (StringHelper.isNullorEmpty(medicationName)){
            throw new IllegalArgumentException("Medication Name is null or empty");
        }else if (StringHelper.isNullorEmpty(medicationManufacturer)){
            throw new IllegalArgumentException("Medication Manufacturer is null or empty");
        } else if (StringHelper.isNullorEmpty(supplierId)) {
            throw new IllegalArgumentException("Supplier ID is null or empty");
        }
        return new Medication.Builder()
                .setMedicationId(medicationId)
                .setMedicationName(medicationName)
                .setMedicationManufacturer(medicationManufacturer)
                .setSupplierId(supplierId)
                .build();
    }
}
