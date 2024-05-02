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
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/rra/v1")
@RequiredArgsConstructor
public class RoomNamesController {


    private final RoomNamesService roomNamesService;


    @PostMapping("/admin/roomNames/save")
    public ResponseEntity<Object> save(@RequestBody RoomsNames roomsNames) {
        Object savedRoomNames = roomNamesService.saveRoomNames(roomsNames);

        if (savedRoomNames instanceof String && ((String) savedRoomNames).startsWith("Error")) {
            return ResponseEntity.badRequest().body(Map.of("error", savedRoomNames));
        } else {
            return ResponseEntity.ok(Map.of("msg", "Room name created successfully", "roomName", savedRoomNames));
        }
    }


    @GetMapping(path = "/client/roomNames/listall")
    public ResponseEntity<List<RoomsNames>>  retrieveRoomNames() {
        List<RoomsNames> roooNames = roomNamesService.retrieveRoomNames();
        return new ResponseEntity<>(roooNames, HttpStatus.OK);
    }

    @GetMapping({"/client/roomNames/get/{id}","/admin/roomNames/get/{id}"})
    public ResponseEntity<Object> getRoomNameById(@PathVariable("id") int roomNameId) {
        RoomsNames roomName = roomNamesService.getRoomNameById(roomNameId);
        if (roomName != null) {
            return ResponseEntity.ok(Map.of("msg", "Room name found", "roomName", roomName));
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @PutMapping("/admin/roomNames/update")
    public ResponseEntity<Object> update(@RequestBody RoomsNames RoomsNames) {
        Object UpdatedRoomNames  =  roomNamesService.updateRoomNames(RoomsNames);
        return ResponseEntity.ok(Map.of("msg", "roomName Updated successfuly", "roomNameUpdated", UpdatedRoomNames));
    }

    @PutMapping("/admin/roomNames/delete")
    public ResponseEntity<Object> delete(@RequestBody RoomsNames deletedRoomName) {
        int id = deletedRoomName.getRoomNameID();
        int newStatus = deletedRoomName.getStatus();

        roomNamesService.deleteRoomNames(id, newStatus);
        return ResponseEntity.ok(Map.of("msg", "roomNAme Deleted successfuly", "id", id));
    }

}