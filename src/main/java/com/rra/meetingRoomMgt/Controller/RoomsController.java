package com.rra.meetingRoomMgt.Controller;

import com.rra.meetingRoomMgt.Service.implementation.RoomsService;
import com.rra.meetingRoomMgt.modal.Bookings;
import com.rra.meetingRoomMgt.modal.Enums.BookingStatus;
import com.rra.meetingRoomMgt.modal.Rooms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/rra/v1/")
public class RoomsController {

    @Value("${upload.directory}")
    private String uploadDirectory;
    @Autowired
    private RoomsService roomsService;

    @PostMapping("admin/rooms/upload")
    public ResponseEntity<String> handleFileUpload(@RequestPart MultipartFile file,
                                                   @RequestParam("roomLocation") String roomLocation,
                                                   @RequestParam("capacity") Integer capacity,
                                                   @RequestParam("roomDescription") String roomDescription) {
        // Remove trailing backslash from uploadDirectory if present
        if (uploadDirectory.endsWith(File.separator)) {
            uploadDirectory = uploadDirectory.substring(0, uploadDirectory.length() - 1);
        }

        // Create the directory if it doesn't exist
        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to create upload directory");
            }
        }

        try {
            // Save the file to the specified directory
            // Save the file to the specified directory
            String fileName = file.getOriginalFilename();
            String filePath = uploadDirectory.replace("\\", "/") + "/" + fileName;

// Log file details
            System.out.println("Received file: " + fileName);
            System.out.println("Saving to: " + filePath);


            // Transfer the file
            file.transferTo(new File(filePath));

            // Construct the image URL for the response
            String baseUrl = "http://localhost:8080/rra/v1/home";
            String relativeImagePath = "/uploads/" + fileName;
            String imageUrl = baseUrl + relativeImagePath;

            // Log the image URL
            System.out.println("Image URL: " + imageUrl);

            // Remove trailing backslash from uploadDirectory if present
            if (uploadDirectory.endsWith(File.separator)) {
                uploadDirectory = uploadDirectory.substring(0, uploadDirectory.length() - 1);
            }


            // Create a new Rooms object with the file details
            Rooms room = new Rooms();
            room.setRoomLocation(roomLocation);
            room.setCapacity(capacity);
            room.setRoomDescription(roomDescription);
            room.setImagePath(imageUrl);
            room.setStatus(1);
            room.setCreatedAt(LocalDateTime.now());

            // Save the Rooms object to the database
            roomsService.saveRoom(room);

            return ResponseEntity.ok("Room created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }



    @PutMapping("admin/rooms/{roomId}")
    public ResponseEntity<String> updateRoomDetails(@PathVariable Integer roomId,
                                                    @RequestBody Rooms updatedRoomDetails) {
        Optional<Rooms> optionalRoom = Optional.ofNullable(roomsService.getRoomById(roomId));
        if (optionalRoom.isPresent()) {
            Rooms room = optionalRoom.get();
            room.setRoomLocation(updatedRoomDetails.getRoomLocation());
            room.setCapacity(updatedRoomDetails.getCapacity());
            room.setRoomDescription(updatedRoomDetails.getRoomDescription());

            roomsService.saveRoom(room);
            return ResponseEntity.ok("Room details updated successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
        }
    }

    @GetMapping("admin/rooms/available")
    public ResponseEntity<List<Rooms>> getAvailableRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<Rooms> availableRooms = roomsService.findAvailableRooms(startTime, endTime);
        return ResponseEntity.ok(availableRooms);
    }


    @GetMapping("/home/rooms/all")
    public ResponseEntity<List<Rooms>> getAllRooms() {
        List<Rooms> rooms = roomsService.getAllRooms();
        if (rooms != null) {
            String baseUrl = "http://localhost:8080/rra/v1/home";

            rooms.forEach(room -> {
                String imagePath = room.getImagePath();
                System.out.println("imagePath: " + imagePath);

                // Check if it's a URL or a local file path
                if (!imagePath.startsWith("http")) {
                    // It's a local file path, construct the URL
                    String fileName = Paths.get(imagePath).getFileName().toString();
                    // Sanitize the file name (replace special characters)
                    fileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_"); // Replace any non-alphanumeric characters with underscores
                    String relativeImagePath = "/uploads/" + fileName;
                    String imageUrl = baseUrl + relativeImagePath;

                    System.out.println("relativeImagePath: " + relativeImagePath);
                    System.out.println("FileName: " + fileName);

                    room.setImagePath(imageUrl);
                }
            });

            return new ResponseEntity<>(rooms, HttpStatus.OK);
        }
        return ResponseEntity.ok(rooms);
    }





    @PutMapping(value = {"/update", "/update/"})
    public ResponseEntity<String> updateRoom(
                                             @RequestParam Integer roomID,
                                             @RequestParam("roomLocation") String roomLocation,
                                             @RequestParam("capacity") Integer capacity,
                                             @RequestParam("roomDescription") String roomDescription) {
        if (roomsService.roomExists(roomID)) {
            Rooms updatedRoom = roomsService.getRoomById(roomID);

            // Update room details
            updatedRoom.setRoomLocation(roomLocation);
            updatedRoom.setCapacity(capacity);
            updatedRoom.setRoomDescription(roomDescription);
            updatedRoom.setUpdatedAt(LocalDateTime.now());

            // Update image if provided
//            if (file != null) {
//                try {
//                    // Handle file upload similar to the @PostMapping method
//                    // Save the file to the specified directory
//                    String fileName = file.getOriginalFilename();
//                    String filePath = uploadDirectory.replace("\\", "/") + "/" + fileName;
//
//                    // Transfer the file
//                    file.transferTo(new File(filePath));
//
//                    // Construct the image URL for the response
//                    String baseUrl = "http://localhost:8080/rra/v1/home";
//                    String relativeImagePath = "/uploads/" + fileName;
//                    String imageUrl = baseUrl + relativeImagePath;
//
//                    // Set the image path in the updated room
//                    updatedRoom.setImagePath(imageUrl);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                            .body("Failed to upload file: " + e.getMessage());
//                }
//            }

            // Save the updated room
            roomsService.saveRoom(updatedRoom);

            return ResponseEntity.ok("Room updated successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/id")
    public ResponseEntity<String> deleteRoom(@RequestParam Integer roomID) {
        if (roomsService.roomExists(roomID)) {
            roomsService.deleteRoom(roomID);
            return ResponseEntity.ok("Room deleted successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
