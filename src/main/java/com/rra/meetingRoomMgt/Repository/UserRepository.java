package com.rra.meetingRoomMgt.Repository;

import com.rra.meetingRoomMgt.modal.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    // Since email is unique, we'll find users by email
    Optional<Users> findByEmail(String email);

    @Query(value = "SELECT nextval('user_no_sequence')", nativeQuery = true)
    Integer getNextValForUserNo();

    boolean existsByEmail(String email);

    boolean existsByEmpNo(String empNo);

    @Modifying
    @Query("UPDATE Users c SET c.userStatus = :newStatus WHERE c.staffID = :id")
    @Transactional
    Object updateUsersByStatus(@Param("id") Integer id, @Param("newStatus") String newStatus);


}
