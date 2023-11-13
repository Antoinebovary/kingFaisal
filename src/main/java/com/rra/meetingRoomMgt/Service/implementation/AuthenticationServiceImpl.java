package com.rra.meetingRoomMgt.Service.implementation;

import com.rra.meetingRoomMgt.dto.request.SignUpRequest;
import com.rra.meetingRoomMgt.dto.request.SigninRequest;
import com.rra.meetingRoomMgt.dto.response.JwtAuthenticationResponse;
import com.rra.meetingRoomMgt.modal.Users;
import com.rra.meetingRoomMgt.Repository.UserRepository;
import com.rra.meetingRoomMgt.Service.AuthenticationService;
import com.rra.meetingRoomMgt.Service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.AuthenticationException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public Users signup(SignUpRequest request) {
            Users user= new Users();

            user.setFullnames(request.getFullnames());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setEmpNo(request.getEmpNo());
            user.setMobileNo(request.getMobileNo());
            user.setEmail(request.getEmail());
            user.setLoginFailCount(0);
            user.setUserStatus("true");
            user.setPosition(request.getPosition());

            // Set created_at and updated_at with the current timestamp
            LocalDateTime currentTimestamp = LocalDateTime.now();
            user.setCreatedAt(currentTimestamp);
            user.setUpdatedAt(currentTimestamp);

            // Explicitly set user_no using the next value from the sequence
            user.setUserNo(userRepository.getNextValForUserNo());

            return userRepository.save(user);
    }

    public JwtAuthenticationResponse signin(SigninRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // If authentication is successful, reset login failure count
            Users user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
            user.setLoginFailCount(0);
            userRepository.save(user);

            // Generate JWT token
            String jwt = jwtService.generateToken(user);
            return JwtAuthenticationResponse.builder().token(jwt).build();
        } catch (AuthenticationException e) {
            // Authentication failed, handle the failure
            handleLoginFailure(request.getEmail());
            throw new IllegalArgumentException("Invalid email or password.");
        }
    }

    private void handleLoginFailure(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // Increment login failure count
        int currentFailCount = user.getLoginFailCount();
        user.setLoginFailCount(currentFailCount + 1);
        userRepository.save(user);

    }


}
