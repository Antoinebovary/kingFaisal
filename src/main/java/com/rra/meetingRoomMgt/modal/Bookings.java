package com.rra.meetingRoomMgt.modal;

import com.rra.meetingRoomMgt.modal.Enums.BookingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Bookings {
    @Id
    @Column(name = "BookingID")
    private Integer bookingID;

    @ManyToOne
    @JoinColumn(name = "RoomID")
    private Rooms room;

    @ManyToOne
    @JoinColumn(name = "StaffID")
    private Users user;

    @Column(name = "StartTime", nullable = false)
    private LocalDate startTime;

    @Column(name = "EndTime", nullable = false)
    private LocalDate endTime;

    @Column(name = "Purpose", nullable = false)
    private String purpose;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
