package com.agrosoft.restAPI.controller;

import com.agrosoft.restAPI.exception.AppException;
import com.agrosoft.restAPI.model.Farm;
import com.agrosoft.restAPI.model.Role;
import com.agrosoft.restAPI.model.RoleName;
import com.agrosoft.restAPI.model.User;
import com.agrosoft.restAPI.payload.ApiResponse;
import com.agrosoft.restAPI.payload.JwtAuthenticationResponse;
import com.agrosoft.restAPI.payload.LoginRequest;
import com.agrosoft.restAPI.payload.SignUpRequest;
import com.agrosoft.restAPI.repository.FarmRepository;
import com.agrosoft.restAPI.repository.RoleRepository;
import com.agrosoft.restAPI.repository.UserRepository;
import com.agrosoft.restAPI.security.CurrentUser;
import com.agrosoft.restAPI.security.JwtTokenProvider;
import com.agrosoft.restAPI.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.Instant;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private FarmRepository farmRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, FarmRepository farmRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.farmRepository = farmRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

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
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest, @CurrentUser UserPrincipal currentUser) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email is already taken!"));
        }

        User user = new User();
        user.setFirst_name(signUpRequest.getFirst_name());
        user.setLast_name(signUpRequest.getLast_name());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setCreated_at(Instant.now());

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        Farm farm = farmRepository.findById(currentUser.getFarm().getFarm_id())
                .orElseThrow(() -> new AppException("Farm not found"));

        user.setFarm(farm);

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
