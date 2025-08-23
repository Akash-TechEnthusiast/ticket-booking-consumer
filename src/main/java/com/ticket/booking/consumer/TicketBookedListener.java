package com.ticket.booking.consumer;

import com.ticket.booking.consumer.entity.ProcessedTicket;
import com.ticket.booking.consumer.entity.TicketBookedEvent;
import com.ticket.booking.consumer.repo.ProcessedTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketBookedListener {
    private final ProcessedTicketRepository repo;

    @KafkaListener(topics = "ticket.booked", groupId = "ticket-processor")
    public void consume(TicketBookedEvent event) {
        System.out.println("Consumed booking: " + event);
        ProcessedTicket t = new ProcessedTicket();
        t.setBookingId(event.getBookingId());
        t.setPassengerName(event.getPassengerName());
        t.setTrainId(event.getTrainId());
        t.setStatus("CONFIRMED"); // logic could check seat availability
        repo.save(t);
    }
}