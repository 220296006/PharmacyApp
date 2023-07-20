package za.ac.cput.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.dao.UserDao;
import za.ac.cput.dto.AuthenticationRequest;
import za.ac.cput.security.JwtUtil;

import java.security.InvalidKeyException;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/18
 * @Time : 21:09
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final JwtUtil jwtUtil;
     @PostMapping("/authenticate")
     public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) throws InvalidKeyException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        final UserDetails user = userDao.findUserByEmail(request.getEmail());
        if (user != null) {
            return ResponseEntity.ok(jwtUtil.generateToken(user));
        }
        return ResponseEntity.status(400).body("Some error occurred");
    }

}