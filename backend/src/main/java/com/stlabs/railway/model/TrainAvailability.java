package com.stlabs.railway.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;

@Entity
@Table(name = "train_availability", uniqueConstraints = {
        @UniqueConstraint(name = "uk_train_availability_date", columnNames = {"train_id", "travel_date"})
})
public class TrainAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;
    @Column(name = "travel_date", nullable = false)
    private LocalDate travelDate;
    @Column(nullable = false)
    private int availableSeats;

    protected TrainAvailability() {}

    public TrainAvailability(Train train, LocalDate travelDate, int availableSeats) {
        this.train = train;
        this.travelDate = travelDate;
        this.availableSeats = availableSeats;
    }

    public Long getId() { return id; }
    public Train getTrain() { return train; }
    public LocalDate getTravelDate() { return travelDate; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
}
