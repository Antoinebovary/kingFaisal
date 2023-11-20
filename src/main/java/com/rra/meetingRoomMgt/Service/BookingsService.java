package com.rra.meetingRoomMgt.Service;

import com.rra.meetingRoomMgt.modal.Bookings;
import java.util.List;


public interface BookingsService {
    Object saveBookings(Bookings bookings);
    List<Bookings> retrieveBookings();
    Object updateBookings(Bookings bookings);
    Object confirmRoom(int id);
    Object cancelBooking(int id);

}
