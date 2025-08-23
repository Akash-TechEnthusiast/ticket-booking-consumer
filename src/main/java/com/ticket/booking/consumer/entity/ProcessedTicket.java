package com.ticket.booking.consumer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ProcessedTicket {
    @Id
    private Long bookingId;
    private String passengerName;
    private String trainId;
    private String status; // CONFIRMED or REJECTED
}