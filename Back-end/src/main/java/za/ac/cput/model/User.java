package za.ac.cput.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

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
@Document(indexName = "users")
public class User  implements UserDetails {
    @Id
    private Long id;
    @NotEmpty(message = "First Name cannot be empty ")
    @Field(type = FieldType.Text)
    private String firstName;
    @Field(type = FieldType.Text)
    private String middleName;
    @Field(type = FieldType.Text)
    @NotEmpty(message = "Last Name cannot be empty ")
    private String lastName;
    @Field(type = FieldType.Text)
    @NotEmpty(message = "Email cannot be empty ")
    @Email(message = "Email Invalid !!. Please enter valid email ")
    @Column(unique = true)
    private String email;
    @Field(type = FieldType.Text)
    @NotEmpty(message = "Password cannot be empty ")
    @NotNull
    private String password;
    @Field(type = FieldType.Text)
    private String address;
    @Field(type = FieldType.Text)
    private String phone;
    @Field(type = FieldType.Text)
    private String imageUrl;
    @Field(type = FieldType.Text)
    private boolean accountNonExpired;
    @Field(type = FieldType.Text)
    private boolean enabled;
    @Field(type = FieldType.Text)
    private boolean isUsingMfa;
    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @Field(type = FieldType.Text)
    private boolean isNotLocked;
    @Transient
    @ManyToMany(fetch = FetchType.EAGER, cascade = ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @Column(name = "confirmations")
    @Transient
    private Confirmation confirmation;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapsId @JoinColumn(name = "image_data_id", referencedColumnName = "id")
    private ImageData imageData;


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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isNotLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}

