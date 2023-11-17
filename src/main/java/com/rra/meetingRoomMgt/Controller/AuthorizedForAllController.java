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

    @GetMapping("")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("welcome home");
    }



}
