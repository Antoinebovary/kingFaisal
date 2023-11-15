package com.rra.meetingRoomMgt.Repository;

import com.rra.meetingRoomMgt.modal.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    @Modifying
    @Query("UPDATE Authority c SET c.status = :newStatus WHERE c.authorityNo = :id")
    @Transactional
    Object updateAuthorityByStatus(@Param("id") Integer id, @Param("newStatus") int newStatus);


}
