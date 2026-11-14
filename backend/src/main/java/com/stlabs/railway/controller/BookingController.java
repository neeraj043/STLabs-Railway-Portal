package com.stlabs.railway.controller;

import com.stlabs.railway.dto.BookingRequest;
import com.stlabs.railway.dto.BookingResponse;
import com.stlabs.railway.dto.CancelBookingRequest;
import com.stlabs.railway.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@Validated
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingResponse> all() {
        return bookingService.findAll().stream().map(BookingResponse::new).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(@Valid @RequestBody BookingRequest request) {
        return new BookingResponse(bookingService.create(request));
    }

    @GetMapping("/{id}")
    public BookingResponse findById(@PathVariable Long id) {
        return new BookingResponse(bookingService.findById(id));
    }

    @PostMapping("/{id}/cancel")
    public BookingResponse cancel(@PathVariable Long id, @Valid @RequestBody CancelBookingRequest request) {
        return new BookingResponse(bookingService.cancel(id, request.getRefundMethod()));
    }
}
