package com.rra.meetingRoomMgt.Service;

import com.rra.meetingRoomMgt.dto.request.RefreshTokenRequest;
import com.rra.meetingRoomMgt.dto.request.SignUpRequest;
import com.rra.meetingRoomMgt.dto.request.SigninRequest;
import com.rra.meetingRoomMgt.dto.response.JwtAuthenticationResponse;
import com.rra.meetingRoomMgt.modal.User_Authority;
import com.rra.meetingRoomMgt.modal.Users;

import java.util.List;

public interface UserAuthenticationService {
    Object signup(SignUpRequest request);
    Object signin(SigninRequest request);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    List<Users> retrieveUsers();
    Object updateUsers(Users UpdateUsers);
    Object deleteUsers(int id, String newStatus);
}
