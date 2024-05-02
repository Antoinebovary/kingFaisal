package com.rra.meetingRoomMgt.Repository;

import com.rra.meetingRoomMgt.modal.ForgotPassword;
import com.rra.meetingRoomMgt.modal.Users;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgetPasswordRepo extends JpaRepository<ForgotPassword,Integer> {
  Optional<ForgotPassword> findByUsersEmail(String email);
@Query("SELECT fp from ForgotPassword fp where fp.OTP=?1 and fp.users=?2")
  Optional<ForgotPassword> findOtpAndEmail(Integer otp, Users users);
}
