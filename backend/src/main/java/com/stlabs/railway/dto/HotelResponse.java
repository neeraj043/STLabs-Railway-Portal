package com.stlabs.railway.dto;

import java.math.BigDecimal;

public class HotelResponse {
    private final String id;
    private final String name;
    private final String city;
    private final String address;
    private final BigDecimal nightlyRate;
    private final String rating;
    private final String description;

    public HotelResponse(String id, String name, String city, String address, BigDecimal nightlyRate,
                         String rating, String description) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.nightlyRate = nightlyRate;
        this.rating = rating;
        this.description = description;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getAddress() { return address; }
    public BigDecimal getNightlyRate() { return nightlyRate; }
    public String getRating() { return rating; }
    public String getDescription() { return description; }
}
