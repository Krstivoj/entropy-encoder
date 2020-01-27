package com.encoder.entropy_ecnoder.controller;

import com.encoder.entropy_ecnoder.exception.AppException;
import com.encoder.entropy_ecnoder.model.Role;
import com.encoder.entropy_ecnoder.model.RoleName;
import com.encoder.entropy_ecnoder.model.User;
import com.encoder.entropy_ecnoder.payload.APIResponse;
import com.encoder.entropy_ecnoder.payload.JWTAuthenticationResponse;
import com.encoder.entropy_ecnoder.payload.LoginRequest;
import com.encoder.entropy_ecnoder.payload.SignUpRequest;
import com.encoder.entropy_ecnoder.repository.RoleRepository;
import com.encoder.entropy_ecnoder.repository.UserRepository;
import com.encoder.entropy_ecnoder.security.CurrentUser;
import com.encoder.entropy_ecnoder.security.JWTTokenProvider;

import com.encoder.entropy_ecnoder.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private
    AuthenticationManager authenticationManager;

    @Autowired
    private
    UserRepository userRepository;

    @Autowired
    private
    RoleRepository roleRepository;

    @Autowired
    private
    PasswordEncoder passwordEncoder;

    @Autowired
    private
    JWTTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new APIResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new APIResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setName(signUpRequest.getName());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));
        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new APIResponse(true, "User registered successfully"));
    }
    @PostMapping("/changepassword")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String,String> requestParams, @CurrentUser UserPrincipal userPrincipal) {

        User user = userRepository.findByUsername(userPrincipal.getUsername()).get();
        if (!passwordEncoder.matches(requestParams.get("oldPassword"),user.getPassword())) {
            return new ResponseEntity<>(new APIResponse(false, "Old password did not matches!"),
                    HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/users/{username}")
                    .buildAndExpand(user.getUsername()).toUri();
            user.setPassword(passwordEncoder.encode(requestParams.get("newPassword")));
            User save = userRepository.save(user);
            return ResponseEntity.created(location).body(new APIResponse(true, "Password changed successfully"));
        }
    }
    @GetMapping("/profile")
    public User getProfile(@CurrentUser UserPrincipal userPrincipal){
        User user = userRepository.findByUsername(userPrincipal.getUsername()).get();
        return user;
    }
}
