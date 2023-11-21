package com.rra.meetingRoomMgt.Controller;


import com.rra.meetingRoomMgt.Service.RoomNamesService;
import com.rra.meetingRoomMgt.modal.RoomsNames;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/rra/v1/roomNames")
@RequiredArgsConstructor
public class RoomNamesController {


    private final RoomNamesService roomNamesService;


    @PostMapping("/save")
    public ResponseEntity<Object> save(@RequestBody RoomsNames roomsNames) {
        Object SavedRoomNames  =  roomNamesService.saveRoomNames(roomsNames);
        return ResponseEntity.ok(Map.of("msg", "roomName created successfuly", "roomName", SavedRoomNames));
    }

    @GetMapping(path = "/listall")
    public ResponseEntity<List<RoomsNames>>  retrieveRoomNames() {
        List<RoomsNames> roooNames = roomNamesService.retrieveRoomNames();
        return new ResponseEntity<>(roooNames, HttpStatus.OK);
    }


    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody RoomsNames RoomsNames) {
        Object UpdatedRoomNames  =  roomNamesService.updateRoomNames(RoomsNames);
        return ResponseEntity.ok(Map.of("msg", "roomName Updated successfuly", "roomNameUpdated", UpdatedRoomNames));
    }

    @PutMapping("/delete")
    public ResponseEntity<Object> delete(@RequestBody RoomsNames deletedRoomName) {
        int id = deletedRoomName.getRoomNameID();
        int newStatus = deletedRoomName.getStatus();

        roomNamesService.deleteRoomNames(id, newStatus);
        return ResponseEntity.ok(Map.of("msg", "roomNAme Deleted successfuly", "id", id));
    }

}
