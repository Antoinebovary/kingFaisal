package com.rra.meetingRoomMgt.Controller;

import com.rra.meetingRoomMgt.Service.RoomsService;
import com.rra.meetingRoomMgt.modal.Rooms;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/rra/v1/client/rooms")
@RequiredArgsConstructor
public class RoomsController  {


    private final RoomsService roomsService;


    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> save(@RequestPart("rooms") Rooms rooms, @RequestPart("imageFile") MultipartFile imageFile) {
        Object newRoom = roomsService.saveRooms(rooms, imageFile);
        return ResponseEntity.ok(Map.of("msg", "Room saved successfully", "roomSaved", newRoom));
    }

    @GetMapping(path = "/listall")
    public ResponseEntity<List<Rooms>> retrieveRooms() {
        List<Rooms> roo = roomsService.retrieveRooms();
        return new ResponseEntity<>(roo, HttpStatus.OK);
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> update(
            @RequestPart("rooms") Rooms rooms,
            @RequestPart("imageFile") MultipartFile imageFile
    ) {
        Object UpdatedRoom = roomsService.updateRooms(rooms, imageFile);
        return ResponseEntity.ok(Map.of("msg", "room Updated successfuly", "roomUpdated", UpdatedRoom));
    }

    @PutMapping("/delete")
    public ResponseEntity<Object> delete(@RequestBody Rooms deletedRoom) {
        int id = deletedRoom.getRoomID();
        int newStatus = deletedRoom.getStatus();
        roomsService.deleteRooms(id, newStatus);
        return ResponseEntity.ok(Map.of("msg", "room Deleted successfuly", "id", id));
    }

    @GetMapping("/downloadFile/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try {
            // Load file from your file system
            File file = new File("C:/Users/STRIKER/Desktop/RoomsPicture" + fileName);

            // Check if file exists
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            // Prepare response headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=" + fileName);

            // Read file content
            byte[] fileContent = Files.readAllBytes(file.toPath());

            // Return file as byte array
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }



}
