package za.ac.cput.factory;
import za.ac.cput.model.Name;
import za.ac.cput.model.User;
import java.time.LocalDateTime;

public class UserFactory {

    public static User createUser(Long id, Name name, String email,
                                  String password, String phone, String imageUrl,
                                  boolean enabled, boolean isUsingMfa, LocalDateTime createdAt){

        return new User.UserBuilder().
                 id(id)
                .name(name)
                .email(email)
                .password(password)
                .phone(phone)
                .imageUrl(imageUrl)
                .enabled(enabled)
                .usingMfa(isUsingMfa)
                .createdAt(createdAt)
                .build();
    }
}
