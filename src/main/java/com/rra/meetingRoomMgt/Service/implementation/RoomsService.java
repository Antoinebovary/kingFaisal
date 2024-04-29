package com.rra.meetingRoomMgt.Service.implementation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rra.meetingRoomMgt.Repository.RoomsRepository;
import com.rra.meetingRoomMgt.modal.Rooms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoomsService {

    private final RoomsRepository roomsRepository;

    @Autowired
    public RoomsService(RoomsRepository roomsRepository) {
        this.roomsRepository = roomsRepository;
    }
    public boolean roomExists(String roomLocation, Integer capacity, String roomDescription) {
        // Check if a room with the same details already exists
        return roomsRepository.existsByRoomLocationAndCapacityAndRoomDescription(roomLocation, capacity, roomDescription);
    }

    public Rooms saveRoom(Rooms room) {
        roomsRepository.save(room);
        return room;
    }

    public Rooms getRoomById(Integer roomID) {
        Optional<Rooms> optionalRoom = roomsRepository.findById(roomID);
        return optionalRoom.orElse(null);
    }

    public List<Rooms> getAllRooms() {
        return roomsRepository.findAll();
    }

    public void deleteRoom(Integer roomID) {
        roomsRepository.deleteById(roomID);
    }

    public boolean roomExists(Integer roomID) {
        return roomsRepository.existsById(roomID);
    }

    @JsonIgnore
    public List<Rooms> findAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {
        return roomsRepository.findAvailableRooms(startTime, endTime);
    }





}
