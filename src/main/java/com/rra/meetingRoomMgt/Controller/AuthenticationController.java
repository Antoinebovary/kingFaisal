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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/rra/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserAuthenticationService userAuthenticationService;
    private final UserRepository userRepository;

    private final JwtService jwtService;


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


    @GetMapping(path = "/listall")
    public ResponseEntity<List<Users>>retrieveUsers() {
        List<Users> users =  userAuthenticationService.retrieveUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody Users updateUsers) {
        Object UpdateUser  =  userAuthenticationService.updateUsers(updateUsers);
        return ResponseEntity.ok(Map.of("msg", "User Updated successfuly", "User", UpdateUser));
    }

    @PutMapping("/delete")
    public ResponseEntity<Object> delete(@RequestBody Users deleteUser) {
        int id = deleteUser.getStaffID();
        String newStatus = deleteUser.getUserStatus();

        userAuthenticationService.deleteUsers(id, newStatus);
        return ResponseEntity.ok(Map.of("msg", "Users Deleted successfuly", "id", id));
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
