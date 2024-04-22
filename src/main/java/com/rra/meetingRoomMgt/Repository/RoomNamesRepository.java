package com.rra.meetingRoomMgt.Repository;

import com.rra.meetingRoomMgt.modal.Rooms;
import com.rra.meetingRoomMgt.modal.RoomsNames;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RoomNamesRepository extends JpaRepository<RoomsNames, Integer> {

    @Modifying
    @Query("UPDATE RoomsNames c SET c.status = :newStatus WHERE c.roomNameID = :id")
    @Transactional
    Object updateAuthorityByStatus(@Param("id") Integer id, @Param("newStatus") int newStatus);
    RoomsNames findByRoomNameAndRoomID(String roomName, Rooms roomID);

}