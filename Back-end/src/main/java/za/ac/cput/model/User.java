package za.ac.cput.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 16:57
 **/

@Data
@SuperBuilder
@AllArgsConstructor
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
public class User {
    @Id
    private Long id;
    @NotEmpty(message = "First Name cannot be empty ")
    private String firstName;
    private String middleName;
    @NotEmpty(message = "Last Name cannot be empty ")
    private String lastName;
    @NotEmpty(message = "Email  cannot be empty ")
    @Email(message = "Email Invalid !!. Please enter valid email ")
    private String email;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    private String address;
    private String phone;
    private String imageUrl;
    private boolean enabled;
    private boolean isUsingMfa;
    private LocalDateTime createdAt;
    private boolean isNotLocked;
}

