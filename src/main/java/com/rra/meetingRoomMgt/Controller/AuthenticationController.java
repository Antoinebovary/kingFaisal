package com.rra.meetingRoomMgt.Controller;

import com.rra.meetingRoomMgt.Repository.UserRepository;
import com.rra.meetingRoomMgt.Service.JwtService;
import com.rra.meetingRoomMgt.dto.request.RefreshTokenRequest;
import com.rra.meetingRoomMgt.dto.response.JwtAuthenticationResponse;
import com.rra.meetingRoomMgt.modal.Users;
import com.rra.meetingRoomMgt.Service.UserAuthenticationService;
import com.rra.meetingRoomMgt.dto.request.SignUpRequest;
import com.rra.meetingRoomMgt.dto.request.SigninRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/rra/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserAuthenticationService userAuthenticationService;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody SignUpRequest request) {

        if (request.getFullnames() == null || request.getEmpNo() == null ||
                request.getPassword() == null || request.getMobileNo() == null ||
                request.getEmail() == null || request.getPosition() == null) {
            return ResponseEntity.badRequest().body(Map.of("msg", "All fields must be provided"));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("msg", "Email already exists"));
        }

        if (userRepository.existsByEmpNo(request.getEmpNo())) {
            return ResponseEntity.badRequest().body(Map.of("msg", "Employee number already exists"));
        }

        if (!request.getMobileNo().matches("\\d+")) {
            return ResponseEntity.badRequest().body(Map.of("msg", "Phone number must contain only numbers"));
        }

        Object SavedUser  =  userAuthenticationService.signup(request);
        return ResponseEntity.ok(Map.of("msg", "account created successfuly", "user", SavedUser));
    }

    @PostMapping("/signin")
    ResponseEntity<Object> signin(@RequestBody SigninRequest request) {
        try {

            Users user = (Users) userAuthenticationService.signin(request);
            // Generate JWT token and refresh token
            String jwt = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

            return ResponseEntity.ok(Map.of("msg", "logged in successfuly", "user", user, "accessToken", jwt, "refreshToken", refreshToken));
        } catch (AuthenticationException e) {
            // Authentication failed, handle the failure
            handleLoginFailure(request.getEmail());
            return ResponseEntity.badRequest().body(Map.of("msg", "Invalid email or password"));
        }

    }

    private void handleLoginFailure(String email) {
        Users user = userRepository.findByEmail(email)
                .orElse(null);

        if (user != null) {
            // Increment login failure count
            int currentFailCount = user.getLoginFailCount();
            user.setLoginFailCount(currentFailCount + 1);
            userRepository.save(user);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(userAuthenticationService.refreshToken(refreshTokenRequest));
    }

    @GetMapping("/profile")
    public UserDetails getUserProfile(Authentication authentication) {
        // Retrieve user details from the authentication object
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                return (UserDetails) principal;
            }
        }
        return null;
    }

}
