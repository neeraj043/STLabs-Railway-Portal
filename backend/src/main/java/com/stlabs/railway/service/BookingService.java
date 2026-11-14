package com.stlabs.railway.service;

import com.stlabs.railway.dto.BookingRequest;
import com.stlabs.railway.model.Booking;
import com.stlabs.railway.model.Train;
import com.stlabs.railway.model.TrainAvailability;
import com.stlabs.railway.repository.BookingRepository;
import com.stlabs.railway.repository.TrainAvailabilityRepository;
import com.stlabs.railway.repository.TrainRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.stlabs.railway.dto.TravelInsuranceRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final TrainRepository trainRepository;
    private final TrainAvailabilityRepository availabilityRepository;

    public BookingService(BookingRepository bookingRepository, TrainRepository trainRepository,
                          TrainAvailabilityRepository availabilityRepository) {
        this.bookingRepository = bookingRepository;
        this.trainRepository = trainRepository;
        this.availabilityRepository = availabilityRepository;
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
    }

    @Transactional
    public Booking create(BookingRequest request) {
        Train train = trainRepository.findById(request.getTrainId())
                .orElseThrow(() -> new IllegalArgumentException("Train not found"));
        List<String> passengers = request.getPassengerNames() == null || request.getPassengerNames().isEmpty()
                ? Collections.singletonList(request.getPassengerName().trim())
                : request.getPassengerNames().stream().map(String::trim).collect(Collectors.toList());
        int seats = request.getPassengerNames() == null || request.getPassengerNames().isEmpty()
                ? request.getSeats() : passengers.size();
        if (request.getReturnTrainId() == null && request.getReturnDate() != null) {
            throw new IllegalArgumentException("A return train is required for a return date");
        }
        if (request.getReturnTrainId() != null && request.getReturnDate() == null) {
            throw new IllegalArgumentException("A return date is required for a return train");
        }
        Train returnTrain = request.getReturnTrainId() == null ? null : trainRepository.findById(request.getReturnTrainId())
                .orElseThrow(() -> new IllegalArgumentException("Return train not found"));
        if (returnTrain != null && returnTrain.getId().equals(train.getId())) {
            throw new IllegalArgumentException("Return train must be different from the outbound train");
        }
        if (returnTrain != null && (!train.getOrigin().equals(returnTrain.getDestination())
                || !train.getDestination().equals(returnTrain.getOrigin()))) {
            throw new IllegalArgumentException("Return train must travel the reverse route");
        }
        if (returnTrain != null && request.getReturnDate().isBefore(request.getTravelDate())) {
            throw new IllegalArgumentException("Return date cannot be before the journey date");
        }
        TrainAvailability outboundAvailability = getOrCreateAvailability(train, request.getTravelDate());
        TrainAvailability returnAvailability = returnTrain == null
                ? null : getOrCreateAvailability(returnTrain, request.getReturnDate());
        if (returnAvailability != null && returnAvailability.getAvailableSeats() < seats) {
            throw new IllegalStateException("Not enough seats available on the return train");
        }
        if (outboundAvailability.getAvailableSeats() < seats) {
            throw new IllegalStateException("Not enough seats available");
        }
        outboundAvailability.setAvailableSeats(outboundAvailability.getAvailableSeats() - seats);
        BigDecimal totalFare = train.getFare().multiply(BigDecimal.valueOf(seats));
        if (returnTrain != null) {
            returnAvailability.setAvailableSeats(returnAvailability.getAvailableSeats() - seats);
            totalFare = totalFare.add(returnTrain.getFare().multiply(BigDecimal.valueOf(seats)));
        }
        availabilityRepository.save(outboundAvailability);
        if (returnAvailability != null) availabilityRepository.save(returnAvailability);
        TravelInsuranceRequest insurance = request.getTravelInsurance();
        if (insurance != null) {
            if (!passengers.get(0).equalsIgnoreCase(insurance.getInsuredName().trim())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "The insured person's name must match the lead passenger");
            }
            if (insurance.getNomineeName().trim().equalsIgnoreCase(insurance.getInsuredName().trim())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "The nominee must be a different person from the insured passenger");
            }
            totalFare = totalFare.add(BigDecimal.valueOf(seats));
        }
        Booking booking = new Booking(train, returnTrain, request.getReturnDate(),
                passengers.get(0), passengers, request.getEmail(), seats, totalFare, request.getTravelDate());
        booking.setMealPreference(request.getMealPreference());
        if (insurance != null) {
            booking.addTravelInsurance(insurance.getInsuredName(), insurance.getInsuredDateOfBirth(),
                    insurance.getInsuredGender(), insurance.getInsuredPhone(), insurance.getInsuredEmail(),
                    insurance.getInsuredAddress(), insurance.getNomineeName(), insurance.getNomineeRelationship(),
                    insurance.getNomineeDateOfBirth(), insurance.getNomineePhone(), seats);
        }
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking cancel(Long id, String refundMethod) {
        Booking booking = findById(id);
        if (!"CONFIRMED".equals(booking.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This booking has already been cancelled");
        }
        TrainAvailability outboundAvailability = getOrCreateAvailability(booking.getTrain(), booking.getTravelDate());
        outboundAvailability.setAvailableSeats(outboundAvailability.getAvailableSeats() + booking.getSeats());
        availabilityRepository.save(outboundAvailability);
        if (booking.getReturnTrain() != null) {
            TrainAvailability returnAvailability = getOrCreateAvailability(booking.getReturnTrain(), booking.getReturnDate());
            returnAvailability.setAvailableSeats(returnAvailability.getAvailableSeats() + booking.getSeats());
            availabilityRepository.save(returnAvailability);
        }
        booking.setStatus("CANCELLED");
        booking.setRefundMethod(refundMethod);
        return bookingRepository.save(booking);
    }

    private TrainAvailability getOrCreateAvailability(Train train, java.time.LocalDate travelDate) {
        return availabilityRepository.findForUpdate(train.getId(), travelDate)
                .orElseGet(() -> new TrainAvailability(train, travelDate, train.getAvailableSeats()));
    }
}
