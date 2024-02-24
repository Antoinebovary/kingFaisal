package com.rra.meetingRoomMgt.Service;

import com.rra.meetingRoomMgt.modal.Bookings;
import java.util.List;
import java.util.Map;


public interface BookingsService {

    Object saveBookings(Integer roomId, Bookings bookings);
    List<Map<String, Object>> retrieveBookingsWithRoomIds();
    Object updateBookings(Bookings bookings);
    Object confirmRoom(int id);
    Object  cancelBooking(int id,String purpose);

    // New methods
    List<Bookings> retrievePendingBookings();
    List<Bookings> retrieveConfirmedBookings();
}
