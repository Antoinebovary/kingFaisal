package com.rra.meetingRoomMgt.Service;

import com.rra.meetingRoomMgt.modal.RoomsNames;

import java.util.List;

public interface RoomNamesService {

    Object saveRoomNames(RoomsNames roomsNames);
    List<RoomsNames> retrieveRoomNames();
    Object updateRoomNames(RoomsNames roomsNames);
    Object deleteRoomNames(int id, int newStatus);


}
