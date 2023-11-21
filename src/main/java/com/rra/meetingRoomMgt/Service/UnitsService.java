package com.rra.meetingRoomMgt.Service;

import com.rra.meetingRoomMgt.modal.Units;

import java.util.List;

public interface UnitsService {
    Object saveUnits(Units units);
    List<Units> getAllUnits();
    Object updateUnits(Units units);
    Object deleteUnits(int id);
    Object findUnitsById(int unitID);
}
