package com.ticket.booking.consumer.service;

import com.ticket.booking.consumer.entity.Train;
import com.ticket.booking.consumer.repo.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatAvailabilityService {

    private final TrainRepository trainRepo; // assume this maps to "train" table

    public boolean checkAndReserveSeat(String trainId) {
        Train train = trainRepo.findById(trainId)
                .orElseThrow(() -> new RuntimeException("Train not found"));

        if (train.getAvailableSeats() > 0) {
            train.setAvailableSeats(train.getAvailableSeats() - 1);
            trainRepo.save(train);
            return true; // seat reserved
        }
        return false; // no seats
    }
}
