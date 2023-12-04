package com.rra.meetingRoomMgt.Service.implementation;

import com.rra.meetingRoomMgt.Repository.RoomsRepository;
import com.rra.meetingRoomMgt.Service.RoomsService;
import com.rra.meetingRoomMgt.modal.Rooms;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomsImpl implements RoomsService {

    private final RoomsRepository roomsRepo;

    @Override
    public Object saveRooms(Rooms rooms, MultipartFile imageFile) {

        Rooms room = new Rooms();

        room.setRoomDescription(rooms.getRoomDescription());
        room.setCapacity(rooms.getCapacity());
        room.setRoomLocation(rooms.getRoomLocation());
        room.setStatus(1);

        LocalDateTime currentTimestamp = LocalDateTime.now();
        room.setCreatedAt(currentTimestamp);
        room.setUpdatedAt(currentTimestamp);

        // Save image file to filesystem and update roomImage field
        if (imageFile != null) {
            String fileName = imageFile.getOriginalFilename();
            String filePath = "C:/Users/STRIKER/Desktop/RoomsPicture" + fileName;

            try {
                imageFile.transferTo(new File(filePath));
                room.setRoomImage(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return roomsRepo.save(room);
    }
    @Override
    public List<Rooms> retrieveRooms() {
        return roomsRepo.findAll();
    }

//    @Override
//    public Object updateRooms(Rooms rooms) {
//
//        Rooms existingRoom = roomsRepo.findById(rooms.getRoomID()).orElse(null);
//        if (existingRoom == null) {
//            return null;
//        }
//
//        int status = existingRoom.getStatus();
//        LocalDateTime createdAt = existingRoom.getCreatedAt();
//
//        existingRoom.setRoomDescription(rooms.getRoomDescription());
//        existingRoom.setCapacity(rooms.getCapacity());
//        existingRoom.setRoomLocation(rooms.getRoomLocation());
//
//        LocalDateTime updatedAt = LocalDateTime.now();
//        existingRoom.setUpdatedAt(updatedAt);
//
//        existingRoom.setStatus(status);
//        existingRoom.setCreatedAt(createdAt);
//
//        return roomsRepo.save(existingRoom);
//    }

    @Override
    public Object updateRooms(Rooms rooms, MultipartFile imageFile) {

        Rooms existingRoom = roomsRepo.findById(rooms.getRoomID()).orElse(null);
        if (existingRoom == null) {
            return null;
        }

        int status = existingRoom.getStatus();
        LocalDateTime createdAt = existingRoom.getCreatedAt();

        existingRoom.setRoomDescription(rooms.getRoomDescription());
        existingRoom.setCapacity(rooms.getCapacity());
        existingRoom.setRoomLocation(rooms.getRoomLocation());

        // Handle image upload
        if (imageFile != null) {
            String fileName = imageFile.getOriginalFilename();
            String filePath = "C:/Users/STRIKER/Desktop/RoomsPicture" + fileName;

            try {
                imageFile.transferTo(new File(filePath));
                existingRoom.setRoomImage(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
