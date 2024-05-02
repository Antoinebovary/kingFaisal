package com.rra.meetingRoomMgt.modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgotPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;
    @Column(nullable = false)
    private Integer OTP;
    @Column(nullable = false)
    private Date ExpiredDate;
    @OneToOne
    private Users users;
}
