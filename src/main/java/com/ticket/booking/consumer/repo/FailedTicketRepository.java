package com.ticket.booking.consumer.repo;

import com.ticket.booking.consumer.entity.FailedTicket;
import com.ticket.booking.consumer.entity.ProcessedTicket;
import org.springframework.data.jpa.repository.JpaRepository;



public interface FailedTicketRepository extends JpaRepository<FailedTicket, Long> {
}