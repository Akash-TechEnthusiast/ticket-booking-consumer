package com.ticket.booking.consumer;

import com.ticket.booking.consumer.entity.ProcessedTicket;
import com.ticket.booking.consumer.entity.TicketBookedEvent;
import com.ticket.booking.consumer.repo.ProcessedTicketRepository;
import com.ticket.booking.consumer.service.SeatAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketBookedListener {
    private final ProcessedTicketRepository repo;
    private final SeatAvailabilityService seatService;
    private final KafkaTemplate<Object, Object> kafka;

    @KafkaListener(topics = "ticket.booked", groupId = "ticket-processor")
    public void consume(TicketBookedEvent event) {
        System.out.println("Consumed booking: " + event);
        if ("12723".equals(event.getTrainId())) {
            //throw new RuntimeException("Failed processing ticket for trainId 12723");
        }
        boolean seatAvailable = seatService.checkAndReserveSeat(event.getTrainId());

        ProcessedTicket t = new ProcessedTicket();
        t.setBookingId(event.getBookingId());
        t.setPassengerName(event.getPassengerName());
        t.setTrainId(event.getTrainId());
        TicketBookedEvent resultEvent;

        if (seatAvailable) {
            t.setStatus("CONFIRMED");
            resultEvent = new TicketBookedEvent(
                    event.getBookingId(),
                    event.getPassengerName(),
                    event.getTrainId(),
                    "CONFIRMED"
            );
        } else {
            t.setStatus("REJECTED"); // or NOT_BOOKED
            resultEvent = new TicketBookedEvent(
                    event.getBookingId(),
                    event.getPassengerName(),
                    event.getTrainId(),
                    "REJECTED"
            );
        }

        repo.save(t);
        kafka.send("ticket.confirmed", event.getBookingId().toString(), resultEvent);
    }
}