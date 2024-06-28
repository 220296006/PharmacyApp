package com.pharmacyapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/18
 * @Time : 21:10
 **/
@Getter
@Setter
@NoArgsConstructor
public class AuthenticationRequest {
    private String email;
    private String password;
}
