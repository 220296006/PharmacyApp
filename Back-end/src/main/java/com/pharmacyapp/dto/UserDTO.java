package com.pharmacyapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pharmacyapp.model.Confirmation;
import com.pharmacyapp.model.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import com.pharmacyapp.model.ImageData;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 17:12
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
public class UserDTO{
    @Id
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String imageUrl;
    private boolean enabled;
    private boolean isUsingMfa;
    private LocalDateTime createdAt;
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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_data")
    private ImageData imageData;
}
