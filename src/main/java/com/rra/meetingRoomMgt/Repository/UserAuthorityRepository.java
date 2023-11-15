package com.rra.meetingRoomMgt.Repository;

import com.rra.meetingRoomMgt.modal.User_Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface UserAuthorityRepository extends  JpaRepository<User_Authority, Integer> {

    @Modifying
    @Query("UPDATE User_Authority c SET c.status = :newStatus WHERE c.id = :id")
    @Transactional
    Object updateAuthorityByStatus(@Param("id") Integer id, @Param("newStatus") int newStatus);


}
