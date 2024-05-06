package com.rra.meetingRoomMgt.modal;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roomNames")
public class RoomsNames {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoomNameID")
    private Integer roomNameID;

    @Column(name = "RoomName", nullable = false)
    private String roomName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RoomID")
    @JsonManagedReference
    private Rooms roomID;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}