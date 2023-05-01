package za.ac.cput.factory;
import za.ac.cput.model.Prescription;
import za.ac.cput.utils.StringHelper;

public class PrescriptionFactory {

    public static Prescription createPrescription(Long prescriptionId, String prescriptionType, String prescriptionDose){

        //Checks if prescriptionId, prescriptionType or prescriptionDose is null or empty
        if(StringHelper.isNullorEmpty(prescriptionType)
                || StringHelper.isNullorEmpty(prescriptionDose)){
            throw new IllegalArgumentException("PrescriptionId or PrescriptionType is null or empty");
        }

        return new Prescription.Builder().setPrescriptionId(prescriptionId)
                .setPrescriptionType(prescriptionType)
                .setPrescriptionDose(prescriptionDose)
                .build();
    }
}
