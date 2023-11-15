package com.rra.meetingRoomMgt.Service;

import com.rra.meetingRoomMgt.modal.Authority;
import com.rra.meetingRoomMgt.modal.User_Authority;

import java.util.List;

public interface UserAuthorityService {

    Object mapRoles(User_Authority userAuthority);
    List<User_Authority> retrieveMappedRoles();
    Object updateUserRoles(User_Authority userAuthority);
    Object deleteUserRoles(int id, int newStatus);

}
