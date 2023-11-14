package com.rra.meetingRoomMgt.Controller;

import com.rra.meetingRoomMgt.Repository.UserRepository;
import com.rra.meetingRoomMgt.Service.JwtService;
import com.rra.meetingRoomMgt.dto.response.JwtAuthenticationResponse;
import com.rra.meetingRoomMgt.modal.Users;
import com.rra.meetingRoomMgt.Service.AuthenticationService;
import com.rra.meetingRoomMgt.dto.request.SignUpRequest;
import com.rra.meetingRoomMgt.dto.request.SigninRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rra/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @PostMapping("/signup")
    public ResponseEntity<Users> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    ResponseEntity<Object> signin(@RequestBody SigninRequest request) {
        try {

            Users user = (Users) authenticationService.signin(request);

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

}
