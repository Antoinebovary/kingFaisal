package com.rra.meetingRoomMgt.dto.request;


import com.rra.meetingRoomMgt.modal.Departments;
import com.rra.meetingRoomMgt.modal.Units;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private String fullnames;
    private String password;
    private String empNo;
    private String mobileNo;
    private String email;
    private String userStatus;
    private Integer loginFailCount;
    private String position;
    private Departments departments;
    private Integer unitID;
    private Units units;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

