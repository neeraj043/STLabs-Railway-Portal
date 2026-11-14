package com.stlabs.railway.dto;

import com.stlabs.railway.model.Train;
import java.math.BigDecimal;
import java.time.LocalTime;

public class TrainResponse {
    private final Long id;
    private final String code;
    private final String name;
    private final String origin;
    private final String destination;
    private final LocalTime departureTime;
    private final LocalTime arrivalTime;
    private final int totalSeats;
    private final int availableSeats;
    private final BigDecimal fare;
    private final String duration;
    private final String serviceFrequency = "DAILY";

    public TrainResponse(Train train) {
        this(train, train.getAvailableSeats());
    }

    public TrainResponse(Train train, int availableSeats) {
        this.id = train.getId();
        this.code = train.getCode();
        this.name = train.getName();
        this.origin = train.getOrigin();
        this.destination = train.getDestination();
        this.departureTime = train.getDepartureTime();
        this.arrivalTime = train.getArrivalTime();
        this.totalSeats = train.getTotalSeats();
        this.availableSeats = availableSeats;
        this.fare = train.getFare();
        int hours = train.getDurationMinutes() / 60;
        int minutes = train.getDurationMinutes() % 60;
        this.duration = hours + "h " + minutes + "m";
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public LocalTime getDepartureTime() { return departureTime; }
    public LocalTime getArrivalTime() { return arrivalTime; }
    public int getTotalSeats() { return totalSeats; }
    public int getAvailableSeats() { return availableSeats; }
    public BigDecimal getFare() { return fare; }
    public String getDuration() { return duration; }
    public String getServiceFrequency() { return serviceFrequency; }
}
