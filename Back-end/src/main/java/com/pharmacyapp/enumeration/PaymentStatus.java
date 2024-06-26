package com.pharmacyapp.enumeration;

import lombok.Getter;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2024/01/15
 * @Time : 18:59
 **/
@Getter
public enum PaymentStatus {

        PAID("PAID"), PENDING("PENDING"), CANCELLED("CANCELLED"), OVERDUE("OVERDUE");

    PaymentStatus(String value) {
    }

}

