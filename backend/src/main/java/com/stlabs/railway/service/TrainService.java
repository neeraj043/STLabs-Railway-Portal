package com.stlabs.railway.service;

import com.stlabs.railway.dto.TrainResponse;
import com.stlabs.railway.model.Train;
import com.stlabs.railway.model.TrainAvailability;
import com.stlabs.railway.repository.TrainAvailabilityRepository;
import com.stlabs.railway.repository.TrainRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TrainService {
    private final TrainRepository trainRepository;
    private final TrainAvailabilityRepository availabilityRepository;

    public TrainService(TrainRepository trainRepository, TrainAvailabilityRepository availabilityRepository) {
        this.trainRepository = trainRepository;
        this.availabilityRepository = availabilityRepository;
    }

    public List<TrainResponse> findAll(LocalDate travelDate) {
        Map<Long, Integer> seatsByTrain = availabilityRepository.findAllByTravelDate(travelDate).stream()
                .collect(Collectors.toMap(availability -> availability.getTrain().getId(),
                        TrainAvailability::getAvailableSeats));
        return trainRepository.findAll().stream()
                .map(train -> new TrainResponse(train, seatsByTrain.getOrDefault(train.getId(), train.getAvailableSeats())))
                .collect(Collectors.toList());
    }
}
