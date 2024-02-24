package com.rra.meetingRoomMgt.Service;

import com.rra.meetingRoomMgt.modal.Units;

import java.util.List;
import java.util.Optional;

public interface UnitsService {
    Object saveUnits(Units units);
    List<Units> getAllUnits();
    Object updateUnits(Units units);
    Object deleteUnits(int id);
    Optional<Units> findUnitsById(int unitID);
}
