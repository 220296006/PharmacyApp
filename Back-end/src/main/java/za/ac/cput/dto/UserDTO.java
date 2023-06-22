package za.ac.cput.dto;

import jakarta.persistence.Embedded;
import lombok.Data;
import za.ac.cput.model.Name;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;
    @Embedded
    private Name name;
    private String email;
    private String phone;
    private String imageUrl;
    private boolean enabled;
    private boolean isUsingMfa;
    private LocalDateTime createdAt;
    private boolean isNotLocked;
}
