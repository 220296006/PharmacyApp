package za.ac.cput.factory;
import za.ac.cput.model.Customer;
import za.ac.cput.model.Name;
import za.ac.cput.utils.StringHelper;

public class CustomerFactory {

    public static Customer createCustomer(Long customerId, String firstName, String middleName, String lastName){

        //Checks if ID or firstName is empty
        if( StringHelper.isNullorEmpty(firstName))
            throw new IllegalArgumentException("Name is null or empty");

        //checks if name is null or not
        Name name = NameFactory.build(firstName, middleName, lastName);
        if (name == null)
            throw new IllegalArgumentException("Name is Null or empty");

        return new Customer.Builder().setCustomerId(customerId)
                .setName(name)
                .build();
    }
}
