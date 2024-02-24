package com.rra.meetingRoomMgt.Repository;

import com.rra.meetingRoomMgt.modal.Bookings;
import com.rra.meetingRoomMgt.modal.Enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingsRepository extends JpaRepository<Bookings, Integer> {

    List<Bookings> findByStatus(BookingStatus status);

}
