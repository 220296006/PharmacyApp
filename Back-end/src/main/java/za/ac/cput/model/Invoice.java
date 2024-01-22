package za.ac.cput.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import za.ac.cput.enumeration.PaymentStatus;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 16:24
 **/

@Data
@SuperBuilder
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
public class Invoice {
    @Id
    private Long id;
    private BigInteger amount;
    private Date dueDate;
    private PaymentStatus paymentStatus;
    private Customer customer;
}
