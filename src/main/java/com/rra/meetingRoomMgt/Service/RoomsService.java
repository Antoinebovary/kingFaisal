package com.rra.meetingRoomMgt.Service;

import com.rra.meetingRoomMgt.modal.Rooms;

import java.util.List;

public interface RoomsService {

    Object saveRooms(Rooms rooms);
    List<Rooms> retrieveRooms();
    Object updateRooms(Rooms rooms);
    Object deleteRooms(int id, int newStatus);


}
