package com.stlabs.railway.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "trains")
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String origin;
    @Column(nullable = false)
    private String destination;
    @Column(nullable = false)
    private LocalTime departureTime;
    @Column(nullable = false)
    private LocalTime arrivalTime;
    @Column(nullable = false)
    private int totalSeats;
    @Column(nullable = false)
    private int availableSeats;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fare;
    @Column(nullable = false)
    private int durationMinutes;

    protected Train() {}

    public Train(String code, String name, String origin, String destination, LocalTime departureTime,
                 LocalTime arrivalTime, int totalSeats, int availableSeats, BigDecimal fare, int durationMinutes) {
        this.code = code;
        this.name = name;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.fare = fare;
        this.durationMinutes = durationMinutes;
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
    public int getDurationMinutes() { return durationMinutes; }

    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
}
