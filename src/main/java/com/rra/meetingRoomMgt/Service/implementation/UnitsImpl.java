package com.rra.meetingRoomMgt.Service.implementation;

import com.rra.meetingRoomMgt.Repository.DepartmentRepository;
import com.rra.meetingRoomMgt.Repository.UnitRepository;
import com.rra.meetingRoomMgt.Service.UnitsService;
import com.rra.meetingRoomMgt.modal.Departments;
import com.rra.meetingRoomMgt.modal.Units;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnitsImpl implements UnitsService {

    private final UnitRepository unitRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public Object saveUnits(Units units) {

        Departments getDpt = departmentRepository.findById(units.getDepartment().getDepartmentID())
                .orElse(null);

        Units unit = new Units();
        unit.setUnitName(units.getUnitName());
        unit.setStatus(1);
        unit.setDepartment(getDpt);

        LocalDateTime currentTimestamp = LocalDateTime.now();
        unit.setCreatedAt(currentTimestamp);
        unit.setUpdatedAt(currentTimestamp);

        return unitRepository.save(unit);
    }

    @Override
    public List<Units> getAllUnits() {


        return unitRepository.findAll();
    }

    @Override
    public Object updateUnits(Units units) {
        Units existingUnits = unitRepository.findById(units.getUnitID()).orElse(null);

        if (existingUnits == null) {
            return null;
        }

        int status = existingUnits.getStatus();
        LocalDateTime createdAt = existingUnits.getCreatedAt();

        existingUnits.setUnitName(units.getUnitName());
        existingUnits.setDepartment(units.getDepartment());

        LocalDateTime updatedAt = LocalDateTime.now();
        existingUnits.setUpdatedAt(updatedAt);

        existingUnits.setStatus(status);
        existingUnits.setCreatedAt(createdAt);

        return unitRepository.save(existingUnits);
    }

    @Override
    public Object deleteUnits(int id) {
        Units existingUnits = unitRepository.findById(id).orElse(null);
        if (existingUnits == null) {
            return null;
        }
        existingUnits.setStatus(0);
        return unitRepository.save(existingUnits);
    }

    @Override
    public Optional<Units> findUnitsById(int unitID) {
        return unitRepository.findById(unitID);
    }
}
