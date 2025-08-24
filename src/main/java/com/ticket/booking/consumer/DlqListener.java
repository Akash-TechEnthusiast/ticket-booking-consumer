package com.ticket.booking.consumer;

import com.ticket.booking.consumer.entity.TicketBookedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class DlqListener {


}
