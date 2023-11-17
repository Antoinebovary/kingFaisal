package com.rra.meetingRoomMgt.Controller;

import com.rra.meetingRoomMgt.Service.UserAuthorityService;
import com.rra.meetingRoomMgt.modal.User_Authority;
import lombok.RequiredArgsConstructor;
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
    public List<User_Authority> retrieveCam() {
        return UserAuthorityService.retrieveMappedRoles();
    }


    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody User_Authority userAuthority) {
        Object UpdateUserdRole  =  UserAuthorityService.updateUserRoles(userAuthority);
        return ResponseEntity.ok(Map.of("msg", "UserRole Updated successfuly", "role", UpdateUserdRole));
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<Object> delete(@RequestBody User_Authority deleteRole) {
        int id = deleteRole.getId();
        int newStatus = deleteRole.getStatus();

        UserAuthorityService.deleteUserRoles(id, newStatus);
        return ResponseEntity.ok(Map.of("msg", "UserRole Deleted successfuly", "id", id));
    }



}
