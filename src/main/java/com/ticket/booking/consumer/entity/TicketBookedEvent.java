package com.ticket.booking.consumer.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketBookedEvent {
    private Long bookingId;
    private String passengerName;
    private String trainId;
    private String status; // BOOKED
}