package com.rra.meetingRoomMgt.Service.implementation;

import com.rra.meetingRoomMgt.Repository.RoomsRepository;
import com.rra.meetingRoomMgt.Service.RoomsService;
import com.rra.meetingRoomMgt.modal.Authority;
import com.rra.meetingRoomMgt.modal.Rooms;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomsImpl implements RoomsService {

    private final RoomsRepository roomsRepo;


    @Override
    public Object saveRooms(Rooms rooms) {

        Rooms room = new Rooms();

        room.setRoomDescription(rooms.getRoomDescription());
        room.setCapacity(rooms.getCapacity());
        room.setRoomLocation(rooms.getRoomLocation());
        room.setStatus(1);

        LocalDateTime currentTimestamp = LocalDateTime.now();
        room.setCreatedAt(currentTimestamp);
        room.setUpdatedAt(currentTimestamp);

        return roomsRepo.save(room);
    }

    @Override
    public List<Rooms> retrieveRooms() {
        return roomsRepo.findAll();
    }

    @Override
    public Object updateRooms(Rooms rooms) {

        Rooms existingRoom = roomsRepo.findById(rooms.getRoomID()).orElse(null);
        if (existingRoom == null) {
            return null;
        }

        int status = existingRoom.getStatus();
        LocalDateTime createdAt = existingRoom.getCreatedAt();

        existingRoom.setRoomDescription(rooms.getRoomDescription());
        existingRoom.setCapacity(rooms.getCapacity());
        existingRoom.setRoomLocation(rooms.getRoomLocation());

        LocalDateTime updatedAt = LocalDateTime.now();
        existingRoom.setUpdatedAt(updatedAt);

        existingRoom.setStatus(status);
        existingRoom.setCreatedAt(createdAt);

        return roomsRepo.save(existingRoom);
    }

    @Override
    public Object deleteRooms(int id, int newStatus) {
        return roomsRepo.updateRoomsByStatus(id ,newStatus);
    }
}
