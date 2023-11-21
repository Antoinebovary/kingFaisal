package com.rra.meetingRoomMgt.Repository;

import com.rra.meetingRoomMgt.modal.Departments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DepartmentRepository extends JpaRepository<Departments, Integer> {

}
