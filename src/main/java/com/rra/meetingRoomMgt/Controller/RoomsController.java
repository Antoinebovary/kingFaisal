package com.rra.meetingRoomMgt.Controller;

import com.rra.meetingRoomMgt.Service.RoomsService;
import com.rra.meetingRoomMgt.modal.Rooms;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/rra/v1/client/rooms")
@RequiredArgsConstructor
public class RoomsController  {


    private final RoomsService roomsService;


    @PostMapping("/save")
    public ResponseEntity<Object> save(@RequestBody Rooms rooms) {
        Object SavedRooms  =  roomsService.saveRooms(rooms);
        return ResponseEntity.ok(Map.of("msg", "room created successfuly", "room", SavedRooms));
    }

    @GetMapping(path = "/listall")
    public ResponseEntity<List<Rooms>> retrieveRooms() {
        List<Rooms> roo = roomsService.retrieveRooms();
        return new ResponseEntity<>(roo, HttpStatus.OK);
    }


    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody Rooms rooms) {
        Object UpdatedRoom  =  roomsService.updateRooms(rooms);
        return ResponseEntity.ok(Map.of("msg", "room Updated successfuly", "roomUpdated", UpdatedRoom));
    }

    @PutMapping("/delete")
    public ResponseEntity<Object> delete(@RequestBody Rooms deletedRoom) {
        int id = deletedRoom.getRoomID();
        int newStatus = deletedRoom.getStatus();

        roomsService.deleteRooms(id, newStatus);
        return ResponseEntity.ok(Map.of("msg", "room Deleted successfuly", "id", id));
    }


}
