package za.ac.cput.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import za.ac.cput.dto.UserDTO;
import za.ac.cput.model.Response;
import za.ac.cput.model.User;
import za.ac.cput.service.UserService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date :  2023/06/22
 * @Time : 17:22
 **/

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> createUser(@RequestBody @Validated User user){
         log.info("Saving A user: {}", user);
         UserDTO userDTO = userService.createUser(user);
         return  ResponseEntity.created(getUri()).body(
                     Response.builder()
                         .timeStamp(now())
                        .data(Map.of("user", userDTO))
                        .message("User Created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
         );

    }
    @GetMapping("/all")
    public ResponseEntity<Response> getAllUsers(@RequestParam Optional<String> name, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize){
        log.info("Fetching users for page {} of size {}:", page, pageSize);
           return ResponseEntity.ok().body(
                   Response.builder()
                    .timeStamp(now())
                    .data(Map.of("page", userService.getAllUsers(name.orElse(""), page.orElse(0), pageSize.orElse(10))))
                    .message("Users retrieved")
                    .status(HttpStatus.OK)
                    .statusCode(OK.value())
                    .build()
           );
    }
    @GetMapping("/read/{id}")
    public ResponseEntity<Response> findUserById(@PathVariable  Long id){
        log.info("Fetching A User By Id: {}", id);
        return  ResponseEntity.ok().body(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("user",  userService.findUserById(id)))
                        .message("User retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateUser(@Valid @RequestBody User user){
           log.info("Update User: {}", user);
           UserDTO userDTO = userService.updateUser(user);
           return  ResponseEntity.created(getUri()).body(
                    Response.builder()
                        .timeStamp(now())
                        .data(Map.of("user", userDTO))
                        .message("User Updated")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
          );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable Long id){
        log.info("Delete User: {}", id);
         return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("user", userService.deleteUser(id)))
                        .message("User Deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @GetMapping("/confirm")
    public ResponseEntity<Response> confirmUserAccount(@RequestParam("token") String token) {
        Boolean isSuccess = userService.verifyToken(token);
        return ResponseEntity.ok().body(
                Response.builder()
                        .timeStamp(LocalDateTime.parse(LocalDateTime.now().toString()))
                        .data(Map.of("Success", isSuccess))
                        .message("Account Verified")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    private URI getUri(){
        return  URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }
}
