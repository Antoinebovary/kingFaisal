package com.rra.meetingRoomMgt.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

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
