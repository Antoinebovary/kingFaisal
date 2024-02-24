package com.rra.meetingRoomMgt.Service.implementation;

import com.rra.meetingRoomMgt.Repository.BookingsRepository;
import com.rra.meetingRoomMgt.Repository.RoomsRepository;
import com.rra.meetingRoomMgt.Repository.UserRepository;
import com.rra.meetingRoomMgt.Service.BookingsService;
import com.rra.meetingRoomMgt.modal.Bookings;
import com.rra.meetingRoomMgt.modal.Enums.BookingStatus;
import com.rra.meetingRoomMgt.modal.Rooms;
import com.rra.meetingRoomMgt.modal.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingsImpl  implements BookingsService {

    private final BookingsRepository bookingsRepo;
    private final RoomsRepository roomsRepository;
    private final UserRepository usersRepository;

    @Override
    public Map<String, Object> saveBookings(Integer roomId, Bookings bookings) {
        // Fetch the room details based on the room ID provided
        Rooms getRoom = roomsRepository.findById(roomId).orElse(null);

        // Check if the room exists
        if (getRoom == null) {
            return Map.of("msg", "Room not found");
        }

        // Associate the booking with the room
        bookings.setRoom(getRoom);

        // Set other booking properties...

        // Save the booking to the database
        LocalDateTime currentTimestamp = LocalDateTime.now();
        bookings.setCreatedAt(currentTimestamp);
        bookings.setStatus(BookingStatus.CONFIRMED);
        bookings.setUpdatedAt(currentTimestamp);

        Bookings savedBooking = bookingsRepo.save(bookings);

        // Construct the response object
        Map<String, Object> response = new HashMap<>();
        response.put("booking", savedBooking);
        response.put("roomId", roomId);

        return response;
    }



    @Override
    public List<Map<String, Object>> retrieveBookingsWithRoomIds() {
        List<Bookings> bookings = bookingsRepo.findAll();
        List<Map<String, Object>> bookingsWithRoomIds = new ArrayList<>();

        for (Bookings booking : bookings) {
            if (booking.getRoom() != null) { // Check if booking has an associated room
                Map<String, Object> bookingWithRoomId = new HashMap<>();
                bookingWithRoomId.put("booking", booking);
                bookingWithRoomId.put("roomId", booking.getRoom().getRoomID());
                bookingsWithRoomIds.add(bookingWithRoomId);
            }
        }

        return bookingsWithRoomIds;
    }




    @Override
    public Object updateBookings(Bookings bookings) {
        Bookings existingBookings = bookingsRepo.findById(bookings.getBookingID()).orElse(null);

        if (existingBookings == null) {
            return null;
        }

        Rooms getRoom = roomsRepository.findById(bookings.getRoom().getRoomID()).orElse(null);
        Users getUser = usersRepository.findById(bookings.getUser().getStaffID()).orElse(null);

        existingBookings.setRoom(getRoom);
        existingBookings.setUser(getUser);
        existingBookings.setStartTime(bookings.getStartTime());
        existingBookings.setEndTime(bookings.getEndTime());
        existingBookings.setPurpose(bookings.getPurpose());

        existingBookings.setUpdatedAt(LocalDateTime.now());
        existingBookings.setStatus(BookingStatus.CONFIRMED);

        return bookingsRepo.save(existingBookings);
    }


    @Override
    public Object confirmRoom(int id) {
        Bookings existingBooking = bookingsRepo.findById(id).orElse(null);

        if (existingBooking == null) {
            return null; // or throw an exception, return an error response, etc.
        }

        // Check the room status
        if (existingBooking.getRoom() != null && "confirmed".equals(existingBooking.getRoom().getStatus())) {
            return "Room cannot be confirmed, it is already confirmed.";
        }

        existingBooking.setStatus(BookingStatus.CONFIRMED);
        return bookingsRepo.save(existingBooking);
    }
    @Override
    public List<Bookings> retrievePendingBookings() {
        return bookingsRepo.findByStatus(BookingStatus.PENDING);
    }

    @Override
    public List<Bookings> retrieveConfirmedBookings() {
        return bookingsRepo.findByStatus(BookingStatus.CONFIRMED);
    }



    @Override
    public Object cancelBooking(int id,String purpose) {
        Bookings existingBooking = bookingsRepo.findById(id).orElse(null);

        if (existingBooking == null) {
            return null;
        }

        existingBooking.setCancelBooking(purpose);

        existingBooking.setStatus(BookingStatus.CANCELED);
        return bookingsRepo.save(existingBooking);
    }
}
