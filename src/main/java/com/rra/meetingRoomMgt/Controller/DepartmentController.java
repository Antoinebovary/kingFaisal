package com.rra.meetingRoomMgt.Controller;

import com.rra.meetingRoomMgt.Service.DepartmentsService;
import com.rra.meetingRoomMgt.modal.Departments;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/rra/v1/admin/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentsService departmentService;

    @GetMapping(path = "/listall")
    public ResponseEntity<List<Departments>> getAllDepartments() {
        List<Departments> departments = departmentService.getAllDpt();
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createDepartment(@RequestBody Departments department) {
        Object result = departmentService.saveDepartments(department);
        if (result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateDepartment(@RequestBody Departments department) {
        Object result = departmentService.updateDepartments(department);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/delete")
    public ResponseEntity<Object> deleteDepartment(@RequestBody Departments department) {
        int id = department.getDepartmentID();
        Object deleteDpt = departmentService.deleteDepartments(id);

        if (deleteDpt != null) {
            return ResponseEntity.ok(Map.of("msg", "dpt deleted successful", "id", id, "deletedDpt", deleteDpt));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("msg", "dpt not found for ID: " + id));
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Object> findDepartmentById(@PathVariable Integer id) {
        Object department = departmentService.findDepartmentById(id);
        if (department != null) {
            return ResponseEntity.ok(department);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
