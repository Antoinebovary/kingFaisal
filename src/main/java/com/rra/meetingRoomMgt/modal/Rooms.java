package com.rra.meetingRoomMgt.modal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_tables")
public class Rooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "RoomID")
    private Integer roomID;

    @Column(name = "RoomLocation", nullable = false)
    private String roomLocation;

    @Column(name = "Capacity", nullable = false)
    @Min(value = 0, message = "Capacity cannot be less than 0")
    @Max(value = 50, message = "Capacity cannot exceed 50")
    private Integer capacity;

    @Column(name = "RoomDescription", nullable = false)
    private String roomDescription;

    @Lob
    @Column(name = "roomImage", nullable = true)
    private byte[] roomImage;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "image_path", nullable = true)
    private String imagePath;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public Rooms(int roomID) {
        this.roomID = roomID;
    }
}