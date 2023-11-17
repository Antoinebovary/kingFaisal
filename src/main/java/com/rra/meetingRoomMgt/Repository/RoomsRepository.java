package com.rra.meetingRoomMgt.Repository;

import com.rra.meetingRoomMgt.modal.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RoomsRepository extends JpaRepository<Rooms, Integer> {

    @Modifying
    @Query("UPDATE Rooms c SET c.status = :newStatus WHERE c.roomID = :id")
    @Transactional
    Object updateRoomsByStatus(@Param("id") Integer id, @Param("newStatus") int newStatus);

}
