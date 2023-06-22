package za.ac.cput.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.ac.cput.dto.UserDTO;
import za.ac.cput.model.Response;
import za.ac.cput.model.User;
import za.ac.cput.service.UserService;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author : Thabiso Matsba
 * @Project : PharmacyApp
 * @Date :  2023/06/22
 * @Time : 17:22
 **/

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestBody @Validated User user){
         UserDTO userDTO = userService.createUser(user);
         return  ResponseEntity.ok(
                    Response.builder()
                        .timeStamp(now())
                        .data(Map.of("user", userDTO))
                        .message("User Created")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());

    }

}
