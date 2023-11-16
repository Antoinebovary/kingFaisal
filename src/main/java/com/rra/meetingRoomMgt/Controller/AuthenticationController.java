package com.rra.meetingRoomMgt.Controller;

import com.rra.meetingRoomMgt.Repository.UserRepository;
import com.rra.meetingRoomMgt.Service.JwtService;
import com.rra.meetingRoomMgt.dto.request.RefreshTokenRequest;
import com.rra.meetingRoomMgt.dto.response.JwtAuthenticationResponse;
import com.rra.meetingRoomMgt.modal.User_Authority;
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
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/rra/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserAuthenticationService userAuthenticationService;
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


    @GetMapping(path = "/listall")
    public List<Users> retrieveCam() {
        return userAuthenticationService.retrieveUsers();
    }


    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody Users updateUsers) {
        Object UpdateUser  =  userAuthenticationService.updateUsers(updateUsers);
        return ResponseEntity.ok(Map.of("msg", "User Updated successfuly", "User", UpdateUser));
    }

    @PutMapping("/updateStatus")
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




}
