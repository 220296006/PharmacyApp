package za.ac.cput.model;
/*Customer.java
 * Entity for the Customer
 * Thabiso Matsaba
 * 18 April 2023
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
public class Customer {
    @Id
    private Long customerId;
    @NotEmpty(message = "firstName cannot be empty ")
    private String firstName;
    private String middleName;
    @NotEmpty(message = "lastName cannot be empty ")
    private String lastName;
    @NotEmpty(message = "Email  cannot be empty ")
    @Email(message = "Email Invalid !!. Please enter valid email ")
    private String email;
    @NotEmpty(message = "Password cannot be empty")
    private String phone;

}

