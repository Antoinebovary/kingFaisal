package com.rra.meetingRoomMgt.Controller;


import com.rra.meetingRoomMgt.Service.BookingsService;
import com.rra.meetingRoomMgt.modal.Bookings;
import com.rra.meetingRoomMgt.modal.Enums.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/rra/v1/client/bookings")
@RequiredArgsConstructor
public class BookingsController {

    private final BookingsService bookingsService;

    @PostMapping("/save")
    public ResponseEntity<Object> save(@RequestBody Bookings bookings) {
        Object SavedBookings  =  bookingsService.saveBookings(bookings);
        return ResponseEntity.ok(Map.of("msg", "booking created successfuly", "booking", SavedBookings));
    }

    @GetMapping(path = "/listall")
    public  ResponseEntity<List<Bookings>>retrieveBookings() {
        List<Bookings> bookings = bookingsService.retrieveBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody Bookings bookings) {
        Object UpdatedBookungs =  bookingsService.updateBookings(bookings);
        return ResponseEntity.ok(Map.of("msg", "booking Updated successfuly", "bookingUpdated", UpdatedBookungs));
    }

    @PutMapping("/book")
    public ResponseEntity<Object> confirmBooking(@RequestBody Bookings bookings) {
        int id = bookings.getBookingID();
        Object bookedBooking = bookingsService.confirmRoom(id);

        if (bookedBooking != null) {
            return ResponseEntity.ok(Map.of("msg", "Booking successful", "id", id, "status", BookingStatus.CONFIRMED));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("msg", "Booking not found for ID: " + id));
        }
    }

    @PutMapping("/cancel")
    public ResponseEntity<Object> cancelBooking(@RequestBody Bookings bookings) {
        int id = bookings.getBookingID();
        Object cancelledBooking = bookingsService.cancelBooking(id);

        if (cancelledBooking != null) {
            return ResponseEntity.ok(Map.of("msg", "Booking cancelled successful", "id", id, "status", BookingStatus.CANCELED));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("msg", "Booking not found for ID: " + id));
        }
    }

}
