package za.ac.cput.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/01/23
 * @Time : 22:40
 **/
@Getter
@Setter
public class AuthenticationResponse {
    private String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }
}
