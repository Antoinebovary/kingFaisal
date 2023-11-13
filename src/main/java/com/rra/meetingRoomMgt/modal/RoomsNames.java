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
@Table(name = "roomNames")
public class RoomsNames {
    @Id
    @Column(name = "RoomNameID")
    private Integer roomNameID;

    @Column(name = "RoomName", nullable = false)
    private String roomName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RoomID")
    private Rooms roomID;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
