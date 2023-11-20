package com.rra.meetingRoomMgt.Repository;

import com.rra.meetingRoomMgt.modal.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingsRepository extends JpaRepository<Bookings, Integer> {
}
