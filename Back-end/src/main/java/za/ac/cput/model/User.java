package za.ac.cput.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
public class User {
    private Long id;
    @Embedded
    private Name name;
    private String email;
    private String password;
    private String phone;
    private String imageUrl;
    private boolean enabled;
    private boolean isUsingMfa;
    private LocalDateTime createdAt;
    private boolean isNotLocked;
  /*  protected User(){}*/

   /* private User(UserBuilder userBuilder){
        this.id = userBuilder.id;
        this.name = userBuilder.name;
        this.email = userBuilder.email;
        this.password = userBuilder.password;
        this.phone = userBuilder.phone;
        this.imageUrl = userBuilder.imageUrl;
        this.enabled = userBuilder.enabled;
        this.isUsingMfa = userBuilder.isUsingMfa;
        this.createdAt = userBuilder.createdAt;
        this.isNotLocked = userBuilder.isNotLocked;
    }*/

    /*public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isUsingMfa() {
        return isUsingMfa;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isNotLocked(){ return isNotLocked;}

    public User setId(Long id ) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name=" + name +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", enabled=" + enabled +
                ", isUsingMfa=" + isUsingMfa +
                ", createdAt=" + createdAt +
                ", isNotLocked=" + isNotLocked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return isEnabled() == user.isEnabled() && isUsingMfa() == user.isUsingMfa() && Objects.equals(getId(), user.getId()) && Objects.equals(getName(), user.getName()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getPhone(), user.getPhone()) && Objects.equals(getImageUrl(), user.getImageUrl()) && Objects.equals(getCreatedAt(), user.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), getPassword(), getPhone(), getImageUrl(), isEnabled(), isUsingMfa(), getCreatedAt());
    }*/

    /*public static class UserBuilder{
    private Long id;
    private Name name;
    private String email;
    private String password;
    private String phone;
    private String imageUrl;
    private boolean enabled;
    private boolean isUsingMfa;
    private LocalDateTime createdAt;

    private boolean isNotLocked;

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder name(Name name) {
            this.name = name;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public UserBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserBuilder usingMfa(boolean usingMfa) {
           this.isUsingMfa = usingMfa;
            return this;
        }

        public UserBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public  UserBuilder isNotLocked(boolean isNotLocked){
            this.isNotLocked = isNotLocked;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }*/
}
