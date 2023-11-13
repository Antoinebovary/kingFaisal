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
@Table(name = "rooms")
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoomID")
    private Long roomID;

    @Column(name = "RoomLocation", nullable = false)
    private String roomLocation;

    @Column(name = "Capacity", nullable = false)
    private Long capacity;

    @Column(name = "RoomDescription", nullable = false)
    private String roomDescription;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
