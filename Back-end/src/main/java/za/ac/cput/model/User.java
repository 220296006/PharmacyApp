package za.ac.cput.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 16:57
 **/

@Data
@SuperBuilder
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
public class User  implements UserDetails {
    @Id
    private Long id;
    @NotEmpty(message = "First Name cannot be empty ")
    private String firstName;
    private String middleName;
    @NotEmpty(message = "Last Name cannot be empty ")
    private String lastName;
    @NotEmpty(message = "Email cannot be empty ")
    @Email(message = "Email Invalid !!. Please enter valid email ")
    @Column(unique = true)
    private String email;
    @NotEmpty(message = "Password cannot be empty ")
    @NotNull
    private String password;
    private String address;
    private String phone;
    private String imageUrl;
    private boolean enabled;
    private boolean isUsingMfa;
    private LocalDateTime createdAt;
    private boolean isNotLocked;
    @ManyToMany(fetch = FetchType.EAGER, cascade = ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @Column(name = "confirmations")
    private Confirmation confirmation;

    public User(Long id, String firstName, String middleName, String lastName, String email, String password, String phone, String address, String imageUrl, boolean enabled, boolean isUsingMfa, boolean isNotLocked, Set<GrantedAuthority> authorities) {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You may implement custom logic if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return isNotLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You may implement custom logic if needed
    }
}

