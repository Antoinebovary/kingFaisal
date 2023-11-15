package com.rra.meetingRoomMgt.Service;

import com.rra.meetingRoomMgt.modal.Authority;

import java.util.List;


public interface AuthorityService {

    Object saveRoles(Authority authority);
    List<Authority> retrieveRoles();
    Object updateRoles(Authority authority);
    Object deleteRoles(int id, int newStatus);


}
