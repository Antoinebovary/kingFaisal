package com.rra.meetingRoomMgt.Controller;

import com.rra.meetingRoomMgt.Service.UserAuthorityService;
import com.rra.meetingRoomMgt.modal.User_Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/rra/v1/userRoles")
@RequiredArgsConstructor
public class UserAuthorityController {

    private final UserAuthorityService  UserAuthorityService;


    @PostMapping("/save")
    public ResponseEntity<Object> save(@RequestBody User_Authority userAuthority) {
        Object SavedUserRole  =  UserAuthorityService.mapRoles(userAuthority);
        return ResponseEntity.ok(Map.of("msg", "UserRole created successfuly", "role", SavedUserRole));
    }

    @GetMapping(path = "/listall")
    public  ResponseEntity<List<User_Authority>> retrieveUserAuthority() {
        List<User_Authority> userAuth = UserAuthorityService.retrieveMappedRoles();
        return new ResponseEntity<>(userAuth, HttpStatus.OK);
    }


    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody User_Authority userAuthority) {
        Object UpdateUserdRole  =  UserAuthorityService.updateUserRoles(userAuthority);
        return ResponseEntity.ok(Map.of("msg", "UserRole Updated successfuly", "role", UpdateUserdRole));
    }

    @PutMapping("/delete")
    public ResponseEntity<Object> delete(@RequestBody User_Authority deleteRole) {
        int id = deleteRole.getId();
        int newStatus = deleteRole.getStatus();

        UserAuthorityService.deleteUserRoles(id, newStatus);
        return ResponseEntity.ok(Map.of("msg", "UserRole Deleted successfuly", "id", id));
    }



}
