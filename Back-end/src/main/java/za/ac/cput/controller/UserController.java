package za.ac.cput.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import za.ac.cput.dto.AuthenticationRequest;
import za.ac.cput.dto.UserDTO;
import za.ac.cput.exception.ImageUploadException;
import za.ac.cput.model.Response;
import za.ac.cput.model.Role;
import za.ac.cput.model.User;
import za.ac.cput.security.JwtTokenProvider;
import za.ac.cput.service.ConfirmationService;
import za.ac.cput.service.UserService;
import za.ac.cput.service.implementation.ImageDataServiceImp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;
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
@ComponentScan
@CrossOrigin(origins = "http://localhost:4200")
@Controller
@EnableJdbcHttpSession
public class UserController {
    private final UserService userService;
    private final ConfirmationService confirmationService;
    private final ImageDataServiceImp imageDataServiceImp;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Retrieve existing session if available
        if (session == null) {
            log.error("Session not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("Session attributes: {}", session.getAttributeNames());
        UserDetails userDetails = (UserDetails) session.getAttribute("user"); // Access UserDetails from session
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("User details retrieved from session: {}", userDetails);
        return null;
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest,
                                                                HttpServletRequest request) {
        log.info("Received login request for user: {}", authenticationRequest.getEmail());
        // Retrieve user from database
        User user = userService.findUserByEmailIgnoreCase(authenticationRequest.getEmail());
        if (user == null) {
            log.warn("User not found with email: {}", authenticationRequest.getEmail());
            throw new UsernameNotFoundException("User not found");
        }
        // Log the entered password
        log.debug("Entered password: {}", authenticationRequest.getPassword());

        // Retrieve the hashed password stored in the database
        String hashedPasswordFromDatabase = user.getPassword();

        // Verify password
        if (!passwordEncoder.matches(authenticationRequest.getPassword(), hashedPasswordFromDatabase)) {
            log.error("Invalid password for user with email: {}", authenticationRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Log password match
        log.debug("Password matched for user with email: {}", authenticationRequest.getEmail());
        try {
            // Authenticate the user
            Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                    authenticationRequest.getPassword());

            Authentication authenticated = authenticationManager.authenticate(authentication);

            // Retrieve user details from the authenticated object
            UserDetails userDetails = (UserDetails) authenticated.getPrincipal();

            // Log user details
            log.info("Authenticated user details: {}", userDetails);

            // Store user details in session
            HttpSession session = request.getSession(true);
            session.setAttribute("userDetails", userDetails);

            // Log session ID
            log.info("Session ID: {}", session.getId());

            // Log session attributes
            Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                log.info("Session attribute - Name: {}, Value: {}", attributeName, session.getAttribute(attributeName));
            }

            // Check if user details are present in session
            if (session.getAttribute("userDetails") == null) {
                log.error("User details not found in session");
            }

            // Map authorities (roles) to Role objects
            Set<Role> roles = userDetails.getAuthorities().stream()
                    .map(authority -> Role.builder().name(authority.getAuthority()).build())
                    .collect(Collectors.toSet());

            // Generate JWT token
            String token = jwtTokenProvider.createToken(userDetails.getUsername(), roles);
            log.info("Generated JWT token for user: {}, Roles: {}", userDetails.getUsername(), roles);

            // Return token in response body
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("user", user);
            responseBody.put("message", "User authenticated successfully");
            return ResponseEntity.ok(responseBody);
        } catch (BadCredentialsException e) {
            log.error("Invalid password for user with email: {}", authenticationRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) { // Catch and log any other exceptions for broader logging coverage
            log.error("Unexpected error during login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @PostMapping("/register")
    public ResponseEntity<Response> createUser(@RequestBody @Validated User user) {
        log.info("Registering a user: {}", user);
        UserDTO userDTO = userService.createUser(user);
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
    public ResponseEntity<Response> getAllUsers(@RequestParam Optional<String> name, @RequestParam Optional<Integer>
            page, @RequestParam Optional<Integer> pageSize){
        log.info("Fetching users for page {} of size {}:", page, pageSize);
           return ResponseEntity.ok().body(
                   Response.builder()
                    .timeStamp(now())
                    .data(Map.of("page", userService.getAllUsers(name.orElse(""),
                            page.orElse(0), pageSize.orElse(10))))
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
    public ResponseEntity<Response> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO user) {
        log.info("Update User: {}: {}", id, user);
        try {
            UserDTO userDTO = userService.updateUser(id, user);
            if (userDTO != null) {
                return ResponseEntity.ok()
                        .body(Response.builder()
                                .timeStamp(now())
                                .data(Map.of("user", userDTO))
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
    @GetMapping("/verify/{token}/account")
    public ResponseEntity<String> confirmUserAccount(@PathVariable("token") String token) {
        String userId = String.valueOf(confirmationService.findTokenByUserId(token));
        StringBuilder html = new StringBuilder();
        if (userId != null) {
            // Token exists, build verification message in HTML
            html.append("<!DOC TYPE html>")
                    .append("<html>")
                    .append("<body>")
                    .append("<h2>User Verification</h2>")
                    .append("<img src=\"https://thabisomatsba.netlify.app/assets/images/PharmacyApp.png\" alt=\"Pharmacy App Logo\" class=\"logo\" style=\"width: 200px; margin-bottom: 10px;\">")
                    .append("<p>Your account has been successfully verified. You can now proceed to login.</p>")
                    .append("</body>")
                    .append("</html>");
        } else {
            // Token not found, build error message
            html.append("Invalid or expired token");
        }

        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html.toString());
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<Map<String, Object>> uploadImage(@PathVariable("id") Long id,
                                                           @RequestParam("image") MultipartFile file) {
        log.info("Uploading User ID {} Image {}", id, file.getOriginalFilename());
        Map<String, Object> response = new HashMap<>();
        try {
            imageDataServiceImp.uploadImage(id, file);
            // Fetch the updated user with the image data
            User user = userService.findUserById(id);
            // Add necessary user data or image URL to the response
            response.put("success", true);
            response.put("message", "Image uploaded successfully");
            response.put("image", user.getImageUrl()); // Add the updated user object
            return ResponseEntity.ok(response);
        } catch (ImageUploadException e) {
            log.error("Error uploading image for User ID {}: {}", id, e.getMessage());
            response.put("success", false);
            response.put("message", "Error uploading image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            log.error("An internal server error occurred during image upload for User ID {}: {}", id, e.getMessage());
            response.put("success", false);
            response.put("message", "An internal server error occurred during upload. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/image/{id}")
    public ResponseEntity<Map<String, Object>> deleteImage(@PathVariable("id") Long id) {
        try {
            // Implement logic to delete the image associated with the user ID
            imageDataServiceImp.deleteImage(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Image deleted successfully"));
        } catch (Exception e) {
            log.error("Error deleting image for User ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Failed to delete image"));
        }
    }


    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getUserImage(@PathVariable("id") Long userId) {
        try {
            // Retrieve the image data for the specified user ID
            byte[] imageData = imageDataServiceImp.getImageData(userId);
            if (imageData != null) {
                // If image data is found, return it with appropriate headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG); // Adjust content type based on image type
                headers.setContentLength(imageData.length);
                return ResponseEntity.ok().headers(headers).body(imageData);
            } else {
                // If no image data found, return a not found response
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // If an error occurs, return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/image/{id}/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable("id") Long id, @RequestParam("image") String fileName){
        byte[] imageData= imageDataServiceImp.downloadImage(id, fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    private URI getUri(){
        return  URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }
}
