package com.ticket.booking.consumer.repo;



import com.ticket.booking.consumer.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainRepository extends JpaRepository<Train, String> {
}
