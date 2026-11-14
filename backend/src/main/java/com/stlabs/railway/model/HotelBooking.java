package com.stlabs.railway.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "hotel_bookings")
public class HotelBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 32)
    private String confirmationNumber;
    @Column(nullable = false)
    private String hotelId;
    @Column(nullable = false)
    private String hotelName;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private LocalDate checkIn;
    @Column(nullable = false)
    private LocalDate checkOut;
    @Column(nullable = false)
    private int guests;
    @Column(nullable = false)
    private String guestName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String ownerEmail;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private Instant createdAt;

    protected HotelBooking() {}

    public HotelBooking(String hotelId, String hotelName, String city, String address, LocalDate checkIn,
                        LocalDate checkOut, int guests, String guestName, String email, String ownerEmail,
                        BigDecimal totalPrice) {
        this.confirmationNumber = "STH-" + UUID.randomUUID().toString().replace("-", "")
                .substring(0, 16).toUpperCase();
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.city = city;
        this.address = address;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guests = guests;
        this.guestName = guestName.trim();
        this.email = email.trim();
        this.ownerEmail = ownerEmail.trim().toLowerCase(java.util.Locale.ROOT);
        this.totalPrice = totalPrice;
        this.status = "CONFIRMED";
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getConfirmationNumber() { return confirmationNumber; }
    public String getHotelId() { return hotelId; }
    public String getHotelName() { return hotelName; }
    public String getCity() { return city; }
    public String getAddress() { return address; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public int getGuests() { return guests; }
    public String getGuestName() { return guestName; }
    public String getEmail() { return email; }
    public String getOwnerEmail() { return ownerEmail; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
