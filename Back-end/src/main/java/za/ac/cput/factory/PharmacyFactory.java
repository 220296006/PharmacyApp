package za.ac.cput.factory;

import za.ac.cput.model.Pharmacy;
import za.ac.cput.utils.StringHelper;

public class PharmacyFactory {

    public static Pharmacy createPharmacy(Long pharmacyId, String pharmacyName, String medicationId, String inventoryId) {

        //Checks if ID is empty

        //Checks if ID or email is empty
        if (StringHelper.isNullorEmpty(pharmacyName))
            throw new IllegalArgumentException("Pharmacy Name is null or empty");

        //Checks if ID or email is empty
        if (StringHelper.isNullorEmpty(medicationId))
            throw new IllegalArgumentException("Medication ID is null or empty");

        //Checks if ID or email is empty
        if (StringHelper.isNullorEmpty(inventoryId))
            throw new IllegalArgumentException("Inventory ID is null or empty");

        return new Pharmacy.Builder()
                .setPharmacyId(pharmacyId)
                .setPharmacyName(pharmacyName)
                .setMedicationId(medicationId)
                .setInventoryId(inventoryId)
                .build();

    }
}
