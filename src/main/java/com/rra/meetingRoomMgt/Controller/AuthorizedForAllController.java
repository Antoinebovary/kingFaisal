package com.rra.meetingRoomMgt.Controller;

import com.rra.meetingRoomMgt.Repository.UserRepository;
import com.rra.meetingRoomMgt.Service.JwtService;
import com.rra.meetingRoomMgt.Service.UserAuthenticationService;
import com.rra.meetingRoomMgt.dto.request.SigninRequest;
import com.rra.meetingRoomMgt.modal.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/rra/v1/home")
@RequiredArgsConstructor
public class AuthorizedForAllController {

    private final UserAuthenticationService userAuthenticationService;
    private final UserRepository userRepository;

    private final JwtService jwtService;

    @GetMapping("")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("welcome home");
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

}
