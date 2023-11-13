package com.rra.meetingRoomMgt.modal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_authority")
public class User_Authority {

    @Id
    @ManyToOne
    @JoinColumn(name = "Authority_no")
    private Authority authority;


    @ManyToOne
    @JoinColumn(name = "user_no")
    private Users user;

    @Column(name = "status", nullable = false)
    private Long status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
