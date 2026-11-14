package com.stlabs.railway.dto;

import com.stlabs.railway.model.HotelBooking;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class HotelBookingResponse {
    private final Long id;
    private final String confirmationNumber;
    private final String hotelName;
    private final String city;
    private final String address;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final int guests;
    private final String guestName;
    private final String email;
    private final BigDecimal totalPrice;
    private final String status;
    private final Instant createdAt;

    public HotelBookingResponse(HotelBooking booking) {
        this.id = booking.getId();
        this.confirmationNumber = booking.getConfirmationNumber();
        this.hotelName = booking.getHotelName();
        this.city = booking.getCity();
        this.address = booking.getAddress();
        this.checkIn = booking.getCheckIn();
        this.checkOut = booking.getCheckOut();
        this.guests = booking.getGuests();
        this.guestName = booking.getGuestName();
        this.email = booking.getEmail();
        this.totalPrice = booking.getTotalPrice();
        this.status = booking.getStatus();
        this.createdAt = booking.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getConfirmationNumber() { return confirmationNumber; }
    public String getHotelName() { return hotelName; }
    public String getCity() { return city; }
    public String getAddress() { return address; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public int getGuests() { return guests; }
    public String getGuestName() { return guestName; }
    public String getEmail() { return email; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
