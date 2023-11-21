package com.rra.meetingRoomMgt.Service.implementation;

import com.rra.meetingRoomMgt.Repository.DepartmentRepository;
import com.rra.meetingRoomMgt.Service.DepartmentsService;
import com.rra.meetingRoomMgt.modal.Authority;
import com.rra.meetingRoomMgt.modal.Departments;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentsImpl implements DepartmentsService {

    private final DepartmentRepository departmentRepository;

    @Override
    public Object saveDepartments(Departments departments) {

        Departments dpt = new Departments();
        dpt.setDepartmentName(departments.getDepartmentName());
        dpt.setStatus(1);

        LocalDateTime currentTimestamp = LocalDateTime.now();
        dpt.setCreatedAt(currentTimestamp);
        dpt.setUpdatedAt(currentTimestamp);

        return departmentRepository.save(dpt);
    }


    @Override
    public List<Departments> getAllDpt() {
        return departmentRepository.findAll();
    }

    @Override
    public Object updateDepartments(Departments departments) {


        Departments existingDepartments = departmentRepository.findById(departments.getDepartmentID()).orElse(null);

        if (existingDepartments == null) {
            return null;
        }

        int status = existingDepartments.getStatus();

        LocalDateTime createdAt = existingDepartments.getCreatedAt();
        existingDepartments.setDepartmentName(departments.getDepartmentName());

        LocalDateTime updatedAt = LocalDateTime.now();
        existingDepartments.setUpdatedAt(updatedAt);

        existingDepartments.setStatus(status);
        existingDepartments.setCreatedAt(createdAt);

        return departmentRepository.save(existingDepartments);

    }

    @Override
    public Object deleteDepartments(int id) {

        Departments existingDept = departmentRepository.findById(id).orElse(null);
        if (existingDept == null) {
            return null;
        }
        existingDept.setStatus(0);
        return departmentRepository.save(existingDept);
    }

    @Override
    public Object findDepartmentById(int departmentId) {
        return departmentRepository.findById(departmentId);
    }

}
