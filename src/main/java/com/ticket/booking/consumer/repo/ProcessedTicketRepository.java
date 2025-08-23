package com.ticket.booking.consumer.repo;

import com.ticket.booking.consumer.entity.ProcessedTicket;
import org.springframework.data.jpa.repository.JpaRepository;




public interface ProcessedTicketRepository extends JpaRepository<ProcessedTicket, Long> {
}