package com.stlabs.railway.dto;

import com.stlabs.railway.model.Booking;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class BookingResponse {
    private final Long id;
    private final Long trainId;
    private final String trainCode;
    private final String trainName;
    private final String origin;
    private final String destination;
    private final Long returnTrainId;
    private final String returnTrainCode;
    private final String returnTrainName;
    private final String returnOrigin;
    private final String returnDestination;
    private final LocalDate returnDate;
    private final String passengerName;
    private final List<String> passengers;
    private final String email;
    private final int seats;
    private final BigDecimal totalFare;
    private final String status;
    private final String mealPreference;
    private final String refundMethod;
    private final String insurancePolicyNumber;
    private final BigDecimal insurancePremium;
    private final int insuranceCoverPerPerson;
    private final LocalDate travelDate;
    private final Instant createdAt;

    public BookingResponse(Booking booking) {
        this.id = booking.getId();
        this.trainId = booking.getTrain().getId();
        this.trainCode = booking.getTrain().getCode();
        this.trainName = booking.getTrain().getName();
        this.origin = booking.getTrain().getOrigin();
        this.destination = booking.getTrain().getDestination();
        this.returnTrainId = booking.getReturnTrain() == null ? null : booking.getReturnTrain().getId();
        this.returnTrainCode = booking.getReturnTrain() == null ? null : booking.getReturnTrain().getCode();
        this.returnTrainName = booking.getReturnTrain() == null ? null : booking.getReturnTrain().getName();
        this.returnOrigin = booking.getReturnTrain() == null ? null : booking.getReturnTrain().getOrigin();
        this.returnDestination = booking.getReturnTrain() == null ? null : booking.getReturnTrain().getDestination();
        this.returnDate = booking.getReturnDate();
        this.passengerName = booking.getPassengerName();
        this.passengers = booking.getPassengers();
        this.email = booking.getEmail();
        this.seats = booking.getSeats();
        this.totalFare = booking.getTotalFare();
        this.status = booking.getStatus();
        this.mealPreference = booking.getMealPreference();
        this.refundMethod = booking.getRefundMethod();
        this.insurancePolicyNumber = booking.getInsurancePolicyNumber();
        this.insurancePremium = booking.getInsurancePremium();
        this.insuranceCoverPerPerson = booking.getInsurancePolicyNumber() == null ? 0 : 200000;
        this.travelDate = booking.getTravelDate();
        this.createdAt = booking.getCreatedAt();
    }

    public Long getId() { return id; }
    public Long getTrainId() { return trainId; }
    public String getTrainCode() { return trainCode; }
    public String getTrainName() { return trainName; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public Long getReturnTrainId() { return returnTrainId; }
    public String getReturnTrainCode() { return returnTrainCode; }
    public String getReturnTrainName() { return returnTrainName; }
    public String getReturnOrigin() { return returnOrigin; }
    public String getReturnDestination() { return returnDestination; }
    public LocalDate getReturnDate() { return returnDate; }
    public String getPassengerName() { return passengerName; }
    public List<String> getPassengers() { return passengers; }
    public String getEmail() { return email; }
    public int getSeats() { return seats; }
    public BigDecimal getTotalFare() { return totalFare; }
    public String getStatus() { return status; }
    public String getMealPreference() { return mealPreference; }
    public String getRefundMethod() { return refundMethod; }
    public String getInsurancePolicyNumber() { return insurancePolicyNumber; }
    public BigDecimal getInsurancePremium() { return insurancePremium; }
    public int getInsuranceCoverPerPerson() { return insuranceCoverPerPerson; }
    public LocalDate getTravelDate() { return travelDate; }
    public Instant getCreatedAt() { return createdAt; }
}
