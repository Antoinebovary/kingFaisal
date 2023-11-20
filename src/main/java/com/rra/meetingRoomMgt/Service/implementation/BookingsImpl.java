package com.rra.meetingRoomMgt.Service.implementation;

import com.rra.meetingRoomMgt.Repository.BookingsRepository;
import com.rra.meetingRoomMgt.Repository.RoomsRepository;
import com.rra.meetingRoomMgt.Repository.UserRepository;
import com.rra.meetingRoomMgt.Service.BookingsService;
import com.rra.meetingRoomMgt.modal.Bookings;
import com.rra.meetingRoomMgt.modal.Enums.BookingStatus;
import com.rra.meetingRoomMgt.modal.Rooms;
import com.rra.meetingRoomMgt.modal.RoomsNames;
import com.rra.meetingRoomMgt.modal.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingsImpl  implements BookingsService {

    private final BookingsRepository bookingsRepo;
    private final RoomsRepository roomsRepository;
    private final UserRepository usersRepository;

    @Override
    public Object saveBookings(Bookings bookings) {

        Rooms getRoom = roomsRepository.findById(bookings.getRoom().getRoomID())
                .orElse(null);

        Users getUser = usersRepository.findById(bookings.getUser().getStaffID())
                .orElse(null);

        Bookings booking = new Bookings();

        booking.setRoom(getRoom);
        booking.setUser(getUser);
        booking.setStartTime(bookings.getStartTime());
        booking.setEndTime(bookings.getEndTime());
        booking.setPurpose(bookings.getPurpose());
        booking.setStatus(BookingStatus.PENDING);

        LocalDateTime currentTimestamp = LocalDateTime.now();
        booking.setCreatedAt(currentTimestamp);
        booking.setUpdatedAt(currentTimestamp);

        return bookingsRepo.save(booking);
    }

    @Override
    public List<Bookings> retrieveBookings() {
        return bookingsRepo.findAll();
    }

    @Override
    public Object updateBookings(Bookings bookings) {
        Bookings existingBookings = bookingsRepo.findById(bookings.getBookingID()).orElse(null);

        if (existingBookings == null) {
            return null;
        }

        BookingStatus status = existingBookings.getStatus();
        LocalDateTime createdAt = existingBookings.getCreatedAt();

        existingBookings.setRoom(bookings.getRoom());
        existingBookings.setUser(bookings.getUser());
        existingBookings.setStartTime(bookings.getStartTime());
        existingBookings.setEndTime(bookings.getEndTime());
        existingBookings.setPurpose(bookings.getPurpose());

        LocalDateTime updatedAt = LocalDateTime.now();
        existingBookings.setUpdatedAt(updatedAt);

        existingBookings.setStatus(status);
        existingBookings.setCreatedAt(createdAt);

        return bookingsRepo.save(existingBookings);
    }

    @Override
    public Object confirmRoom(int id) {

        Bookings existingBooking = bookingsRepo.findById(id).orElse(null);

        if (existingBooking == null) {
            return null;
        }

        existingBooking.setStatus(BookingStatus.CONFIRMED);
        return bookingsRepo.save(existingBooking);
    }

    @Override
    public Object cancelBooking(int id) {
        Bookings existingBooking = bookingsRepo.findById(id).orElse(null);

        if (existingBooking == null) {
            return null;
        }

        existingBooking.setStatus(BookingStatus.CANCELED);
        return bookingsRepo.save(existingBooking);
    }
}
