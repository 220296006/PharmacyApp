package za.ac.cput.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.Id;

import java.util.Objects;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 17:33
 **/

@Data
@SuperBuilder
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
public class Customer{
    @Id
    private Long id;
    private String city;
    private String state;
    private String zipCode;
    private User user;
}
