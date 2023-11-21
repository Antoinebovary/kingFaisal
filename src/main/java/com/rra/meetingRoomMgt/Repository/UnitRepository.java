package com.rra.meetingRoomMgt.Repository;

import com.rra.meetingRoomMgt.modal.Units;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UnitRepository extends JpaRepository<Units, Integer> {
}
