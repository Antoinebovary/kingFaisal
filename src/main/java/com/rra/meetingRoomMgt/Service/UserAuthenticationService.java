package com.rra.meetingRoomMgt.Service;

import com.rra.meetingRoomMgt.dto.request.RefreshTokenRequest;
import com.rra.meetingRoomMgt.dto.request.SignUpRequest;
import com.rra.meetingRoomMgt.dto.request.SigninRequest;
import com.rra.meetingRoomMgt.dto.response.JwtAuthenticationResponse;

public interface UserAuthenticationService {
    Object signup(SignUpRequest request);
    Object signin(SigninRequest request);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
