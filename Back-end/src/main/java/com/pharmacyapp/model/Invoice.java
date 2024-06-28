package com.pharmacyapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.pharmacyapp.enumeration.PaymentStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

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
