package com.rra.meetingRoomMgt.Service.implementation;

import com.rra.meetingRoomMgt.dto.request.RefreshTokenRequest;
import com.rra.meetingRoomMgt.dto.request.SignUpRequest;
import com.rra.meetingRoomMgt.dto.request.SigninRequest;
import com.rra.meetingRoomMgt.dto.response.JwtAuthenticationResponse;
import com.rra.meetingRoomMgt.modal.Users;
import com.rra.meetingRoomMgt.Repository.UserRepository;
import com.rra.meetingRoomMgt.Service.UserAuthenticationService;
import com.rra.meetingRoomMgt.Service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
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
            user.setUserStatus("active");
            user.setPosition(request.getPosition());

            // Set created_at and updated_at with the current timestamp
            LocalDateTime currentTimestamp = LocalDateTime.now();
            user.setCreatedAt(currentTimestamp);
            user.setUpdatedAt(currentTimestamp);

            // Explicitly set user_no using the next value from the sequence
            user.setUserNo(userRepository.getNextValForUserNo());

            return userRepository.save(user);
    }

    public Object signin(SigninRequest request) {
           return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())).getPrincipal();
    }


    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getAccessToken());
        Users user = userRepository.findByEmail(userEmail).orElseThrow();
        if (jwtService.isTokenValid(refreshTokenRequest.getAccessToken(), user)) {
            String jwt = jwtService.generateToken(user);

            return JwtAuthenticationResponse.builder()
                    .accessToken(jwt)
                    .refreshToken(refreshTokenRequest.getAccessToken())
                    .build();
        }
        return null;
    }
}
