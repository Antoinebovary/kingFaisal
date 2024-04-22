package com.rra.meetingRoomMgt.Service.implementation;


import com.rra.meetingRoomMgt.Repository.RoomNamesRepository;
import com.rra.meetingRoomMgt.Repository.RoomsRepository;
import com.rra.meetingRoomMgt.Service.RoomNamesService;
import com.rra.meetingRoomMgt.modal.Rooms;
import com.rra.meetingRoomMgt.modal.RoomsNames;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomNamesImpl implements RoomNamesService {

    private final RoomNamesRepository roomNamesRepository;
    private final RoomsRepository roomsRepository;

    @Override
    public Object saveRoomNames(RoomsNames roomsNames) {
        // Retrieve the Rooms entity using RoomID
        Rooms getRoom = roomsRepository.findById(roomsNames.getRoomID().getRoomID())
                .orElse(null);

        if (getRoom == null) {
            // Handle the case where the room does not exist
            return "Error: Room not found";
        }

        // Check if a room with the same name already exists
        RoomsNames existingRoomName = roomNamesRepository.findByRoomNameAndRoomID(roomsNames.getRoomName(), getRoom);

        if (existingRoomName != null) {
            // Handle the case where the room name already exists
            return "Error: Room name already exists";
        }

        RoomsNames roomNames = new RoomsNames();
        roomNames.setRoomName(roomsNames.getRoomName());
        roomNames.setRoomID(getRoom);
        roomNames.setStatus(1);

        LocalDateTime currentTimestamp = LocalDateTime.now();
        roomNames.setCreatedAt(currentTimestamp);
        roomNames.setUpdatedAt(currentTimestamp);

        return roomNamesRepository.save(roomNames);
    }


    @Override
    public List<RoomsNames> retrieveRoomNames() {
        return roomNamesRepository.findAll();
    }

    @Override
    public Object updateRoomNames(RoomsNames roomsNames) {
        RoomsNames existingRoomsNames = roomNamesRepository.findById(roomsNames.getRoomNameID()).orElse(null);

        if (existingRoomsNames == null) {
            return null;
        }

        int status = existingRoomsNames.getStatus();
        LocalDateTime createdAt = existingRoomsNames.getCreatedAt();

        existingRoomsNames.setRoomName(roomsNames.getRoomName());
        existingRoomsNames.setRoomID(roomsNames.getRoomID());

        LocalDateTime updatedAt = LocalDateTime.now();
        existingRoomsNames.setUpdatedAt(updatedAt);

        existingRoomsNames.setStatus(status);
        existingRoomsNames.setCreatedAt(createdAt);

        return roomNamesRepository.save(existingRoomsNames);
    }

    @Override
    public Object deleteRoomNames(int id, int newStatus) {
        return roomNamesRepository.updateAuthorityByStatus(id, newStatus);
    }
    @Override
    public RoomsNames getRoomNameById(int roomNameId) {
        return roomNamesRepository.findById(roomNameId).orElse(null);
    }
}