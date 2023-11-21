package com.rra.meetingRoomMgt.Service;

import com.rra.meetingRoomMgt.modal.Departments;

import java.util.List;

public interface DepartmentsService {
    Object saveDepartments(Departments departments);
    List<Departments> getAllDpt();
    Object updateDepartments(Departments departments);
    Object deleteDepartments(int id);
    Object findDepartmentById(int departmentId);
}
