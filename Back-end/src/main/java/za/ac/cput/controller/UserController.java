package za.ac.cput.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import za.ac.cput.dto.AuthenticationRequest;
import za.ac.cput.dto.UserDTO;
import za.ac.cput.dto.UserUpdateDTO;
import za.ac.cput.model.AuthenticationResponse;
import za.ac.cput.model.Response;
import za.ac.cput.model.Role;
import za.ac.cput.model.User;
import za.ac.cput.security.JwtTokenProvider;
import za.ac.cput.service.UserService;
import za.ac.cput.service.implementation.UserDetailsServiceImpl;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

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
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired // Autowired the password encoder
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
        log.info("Received login request for user: {}", authenticationRequest.getEmail());

        // Retrieve user from database
        User user = userService.findUserByEmailIgnoreCase(authenticationRequest.getEmail());
        try {
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            // Use Spring Security's recommended authentication approach
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
            // Retrieve user details for token generation
            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

           // Map authorities (roles) to Role objects
            Set<Role> roles = userDetails.getAuthorities().stream()
                    .map(authority -> Role.builder().name(authority.getAuthority()).build())
                    .collect(Collectors.toSet());
           // Generate JWT token
            String token = jwtTokenProvider.createToken(authenticationRequest.getEmail(), roles);
            // Return token in response body
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("message", "User authenticated successfully");
            return ResponseEntity.ok(responseBody);
        } catch (BadCredentialsException e) {
            log.error("Invalid password for user with email {}", authenticationRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PostMapping("/register")
    public ResponseEntity<Response> createUser(@RequestBody @Validated User user) {
        log.info("Registering a user: {}", user);
        UserDTO userDTO = userService.createUser(user);
        // Generate JWT token for the newly registered user
        String token = jwtTokenProvider.createToken(userDTO.getEmail(), userDTO.getRoles());
        return ResponseEntity.created(getUri()).body(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("user", userDTO))
                        .message("User Created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build());
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

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO user) {
        log.info("Update User: {}: {}", id, user);
        try {
            UserUpdateDTO userUpdateDTO = userService.updateSysAdmin(id, user);
            if (userUpdateDTO != null) {
                return ResponseEntity.ok()
                        .body(Response.builder()
                                .timeStamp(now())
                                .data(Map.of("user", userUpdateDTO))
                                .message("User Updated")
                                .status(OK)
                                .statusCode(HttpStatus.OK.value())
                                .build());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Response.builder()
                                .timeStamp(now())
                                .message("User not found for id: " + id)
                                .status(NOT_FOUND)
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .build());
            }
        } catch (Exception e) {
            log.error("Error updating user with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.builder()
                            .timeStamp(now())
                            .message("Failed to update user.")
                            .status(INTERNAL_SERVER_ERROR)
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build());
        }
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
