package com.stlabs.railway.controller;

import com.stlabs.railway.dto.HotelBookingRequest;
import com.stlabs.railway.dto.HotelBookingResponse;
import com.stlabs.railway.dto.HotelResponse;
import com.stlabs.railway.model.HotelBooking;
import com.stlabs.railway.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Validated
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/hotels")
    public List<HotelResponse> hotels(@RequestParam String city) {
        return hotelService.findHotels(city);
    }

    @GetMapping("/hotel-bookings")
    public List<HotelBookingResponse> bookings(Authentication authentication) {
        return hotelService.findBookings(authentication.getName()).stream()
                .map(HotelBookingResponse::new).collect(Collectors.toList());
    }

    @PostMapping("/hotel-bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public HotelBookingResponse createBooking(@Valid @RequestBody HotelBookingRequest request,
                                              Authentication authentication) {
        HotelBooking booking = hotelService.createBooking(request, authentication.getName());
        return new HotelBookingResponse(booking);
    }
}
