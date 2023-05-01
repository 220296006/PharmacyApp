package za.ac.cput.factory;
import za.ac.cput.model.Name;
import za.ac.cput.utils.StringHelper;

public class NameFactory {

    public static Name build(String firstName, String middleName, String lastName){

        if(StringHelper.isNullorEmpty(firstName) || StringHelper.isNullorEmpty(lastName))
            throw new IllegalArgumentException();

        if(StringHelper.isNullorEmpty(middleName))
            middleName = "";

        return new Name.Builder().setFirstName(firstName)
                .setMiddleName(middleName)
                .setLastName(lastName)
                .build();
        }
    }
