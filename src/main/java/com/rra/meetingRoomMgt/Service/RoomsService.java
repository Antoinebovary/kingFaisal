package com.rra.meetingRoomMgt.Service;

import com.rra.meetingRoomMgt.modal.Rooms;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface RoomsService {

    Object saveRooms(Rooms rooms, MultipartFile imageFile);
    List<Rooms> retrieveRooms();
//    Object updateRooms(Rooms rooms);
    Object updateRooms(Rooms rooms, MultipartFile imageFile);
    Object deleteRooms(int id, int newStatus);

}
