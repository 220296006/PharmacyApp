package za.ac.cput.enumeration;

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

        private final String value;

        PaymentStatus(String value) {
            this.value = value;
        }

}

