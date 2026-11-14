package com.stlabs.railway.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.OrderColumn;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "return_train_id")
    private Train returnTrain;
    private LocalDate returnDate;
    @Column(nullable = false)
    private String passengerName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private int seats;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalFare;
    @Column(nullable = false)
    private String status;
    private String mealPreference;
    private String refundMethod;
    @Column(unique = true, length = 32)
    private String insurancePolicyNumber;
    private BigDecimal insurancePremium;
    private String insuredName;
    private LocalDate insuredDateOfBirth;
    private String insuredGender;
    private String insuredPhone;
    private String insuredEmail;
    @Column(length = 250)
    private String insuredAddress;
    private String nomineeName;
    private String nomineeRelationship;
    private LocalDate nomineeDateOfBirth;
    private String nomineePhone;
    @Column(nullable = false)
    private LocalDate travelDate;
    @Column(nullable = false)
    private Instant createdAt;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "booking_passengers", joinColumns = @JoinColumn(name = "booking_id"))
    @OrderColumn(name = "passenger_order")
    @Column(name = "passenger_name", nullable = false, length = 100)
    private List<String> passengers = new ArrayList<>();

    protected Booking() {}

    public Booking(Train train, Train returnTrain, LocalDate returnDate, String passengerName, List<String> passengers,
                   String email, int seats, BigDecimal totalFare, LocalDate travelDate) {
        this.train = train;
        this.returnTrain = returnTrain;
        this.returnDate = returnDate;
        this.passengerName = passengerName;
        this.passengers = new ArrayList<>(passengers);
        this.email = email;
        this.seats = seats;
        this.totalFare = totalFare;
        this.travelDate = travelDate;
        this.status = "CONFIRMED";
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public Train getTrain() { return train; }
    public Train getReturnTrain() { return returnTrain; }
    public LocalDate getReturnDate() { return returnDate; }
    public String getPassengerName() { return passengerName; }
    public List<String> getPassengers() { return new ArrayList<>(passengers); }
    public String getEmail() { return email; }
    public int getSeats() { return seats; }
    public BigDecimal getTotalFare() { return totalFare; }
    public String getStatus() { return status; }
    public String getMealPreference() { return mealPreference; }
    public String getRefundMethod() { return refundMethod; }
    public String getInsurancePolicyNumber() { return insurancePolicyNumber; }
    public BigDecimal getInsurancePremium() { return insurancePremium; }
    public String getInsuredName() { return insuredName; }
    public LocalDate getInsuredDateOfBirth() { return insuredDateOfBirth; }
    public String getInsuredGender() { return insuredGender; }
    public String getInsuredPhone() { return insuredPhone; }
    public String getInsuredEmail() { return insuredEmail; }
    public String getInsuredAddress() { return insuredAddress; }
    public String getNomineeName() { return nomineeName; }
    public String getNomineeRelationship() { return nomineeRelationship; }
    public LocalDate getNomineeDateOfBirth() { return nomineeDateOfBirth; }
    public String getNomineePhone() { return nomineePhone; }
    public LocalDate getTravelDate() { return travelDate; }
    public Instant getCreatedAt() { return createdAt; }
    public void setStatus(String status) { this.status = status; }
    public void setMealPreference(String mealPreference) { this.mealPreference = mealPreference; }
    public void setRefundMethod(String refundMethod) { this.refundMethod = refundMethod; }
    public void addTravelInsurance(String insuredName, LocalDate insuredDateOfBirth, String insuredGender,
                                   String insuredPhone, String insuredEmail, String insuredAddress,
                                   String nomineeName, String nomineeRelationship, LocalDate nomineeDateOfBirth,
                                   String nomineePhone, int insuredPassengers) {
        this.insurancePolicyNumber = "STI-" + UUID.randomUUID().toString().replace("-", "")
                .substring(0, 16).toUpperCase();
        this.insurancePremium = BigDecimal.valueOf(insuredPassengers);
        this.insuredName = insuredName.trim();
        this.insuredDateOfBirth = insuredDateOfBirth;
        this.insuredGender = insuredGender;
        this.insuredPhone = insuredPhone;
        this.insuredEmail = insuredEmail.trim();
        this.insuredAddress = insuredAddress.trim();
        this.nomineeName = nomineeName.trim();
        this.nomineeRelationship = nomineeRelationship;
        this.nomineeDateOfBirth = nomineeDateOfBirth;
        this.nomineePhone = nomineePhone;
    }
}
