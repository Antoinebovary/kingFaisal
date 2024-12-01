package com.rra.meetingRoomMgt.Controller;


import com.rra.meetingRoomMgt.Service.BookingsService;
import com.rra.meetingRoomMgt.modal.Bookings;
import com.rra.meetingRoomMgt.modal.Enums.BookingStatus;
import com.rra.meetingRoomMgt.modal.Rooms;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/rra/v1")
@RequiredArgsConstructor
public class BookingsController {

    private final BookingsService bookingsService;

    @PostMapping("/client/bookings/save")
    public ResponseEntity<Object> save(@RequestBody Bookings bookings) {
        Integer roomId = bookings.getRoom().getRoomID();

        Object savedBookings = bookingsService.saveBookings(roomId, bookings);


           return ResponseEntity.ok(Map.of(
                   "msg", "booking created successfully",
                   "booking", savedBookings,
                   "roomID",roomId
           ));

    }


    @GetMapping("/bookings/listall")
    public ResponseEntity<List<Map<String, Object>>> retrieveBookingsWithRoomIds() {
        List<Map<String, Object>> bookingsWithRoomIds = bookingsService.retrieveBookingsWithRoomIds();
        return ResponseEntity.ok(bookingsWithRoomIds);
    }

//    @GetMapping(path = "/listAvailable")
//    public  ResponseEntity<List<Bookings>>retrieveAvailableBookings() {
//        List<Bookings> bookings = bookingsService.retrieveBookings();
//        return new ResponseEntity<>(bookings, HttpStatus.OK);
//    }



    @GetMapping("/admin/bookings/listPending")
    public ResponseEntity<List<Bookings>> retrievePendingBookings() {
        List<Bookings> pendingBookings = bookingsService.retrievePendingBookings();
        return new ResponseEntity<>(pendingBookings, HttpStatus.OK);
    }

    @GetMapping("/admin/bookings/listConfirmed")
    public ResponseEntity<List<Bookings>> retrieveConfirmedBookings() {
        List<Bookings> confirmedBookings = bookingsService.retrieveConfirmedBookings();


        confirmedBookings.forEach(booking -> {
            Rooms room = booking.getRoom();
            booking.setRoom(room);
        });

        return new ResponseEntity<>(confirmedBookings, HttpStatus.OK);
    }




    @PutMapping("/admin/bookings/update")
    public ResponseEntity<Object> update(@RequestBody Bookings bookings) {
        Object UpdatedBookungs =  bookingsService.updateBookings(bookings);
        return ResponseEntity.ok(Map.of("msg", "booking Updated successfuly", "bookingUpdated", UpdatedBookungs));
    }

    @PutMapping("/admin/bookings/book")
    public ResponseEntity<Object> confirmBooking(@RequestBody Bookings bookings) {
        int id = bookings.getBookingID();
        Rooms room = bookings.getRoom(); // Assuming you have a method to get the room associated with the booking


        if (room != null && BookingStatus.CONFIRMED.equals(bookings.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Room cannot be booked, it is already confirmed.");
        }

        Object bookedBooking = bookingsService.confirmRoom(id);

        if (bookedBooking != null) {
            return ResponseEntity.ok(Map.of("msg", "Booking successful", "id", id, "status", BookingStatus.CONFIRMED));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("msg", "Booking not found for ID: " + id));
        }
    }



    @DeleteMapping("/admin/cancel")
    public ResponseEntity<Object> cancelBookingAdmin(@RequestBody Bookings request) {
        int bookingID = request.getBookingID();
        String purpose = request.getPurpose();
 
        Object cancelledBooking = bookingsService.cancelBooking(bookingID, purpose);

        if (cancelledBooking != null) {
            return ResponseEntity.ok(Map.of("msg", "Booking cancelled successful", "id", bookingID, "status", BookingStatus.CANCELED,"purpose",purpose));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("msg", "Booking not found for ID: " + bookingID));
        }
    }


    @DeleteMapping("/client/bookings/cancel")
    public ResponseEntity<Object> cancelBookingUser(@RequestBody Bookings request) {
        // Call the service method to cancel the booking
        int bookingID = request.getBookingID();
        String purpose = request.getPurpose();
        Object cancelledBooking = bookingsService.cancelBooking(bookingID, purpose);

        if (cancelledBooking != null) {
            return ResponseEntity.ok(Map.of("msg", "Booking cancelled successfully", "id", bookingID, "status", BookingStatus.CANCELED, "purpose", purpose));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("msg", "Booking not found for ID: " + bookingID));
        }
    }




}
