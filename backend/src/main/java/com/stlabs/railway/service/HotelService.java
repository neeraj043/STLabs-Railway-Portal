package com.stlabs.railway.service;

import com.stlabs.railway.dto.HotelBookingRequest;
import com.stlabs.railway.dto.HotelResponse;
import com.stlabs.railway.model.HotelBooking;
import com.stlabs.railway.model.Train;
import com.stlabs.railway.repository.HotelBookingRepository;
import com.stlabs.railway.repository.TrainRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class HotelService {
    private final HotelBookingRepository hotelBookingRepository;
    private final TrainRepository trainRepository;

    public HotelService(HotelBookingRepository hotelBookingRepository, TrainRepository trainRepository) {
        this.hotelBookingRepository = hotelBookingRepository;
        this.trainRepository = trainRepository;
    }

    public List<HotelResponse> findHotels(String city) {
        String normalizedCity = city == null ? "" : city.trim();
        boolean destination = trainRepository.findAll().stream()
                .map(Train::getDestination).map(HotelService::cityName)
                .anyMatch(availableCity -> availableCity.equalsIgnoreCase(normalizedCity));
        if (!destination) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hotel listings found for this destination city");
        }
        String citySlug = normalizedCity.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-");
        List<String> names = Arrays.asList(normalizedCity + " Grand Hotel",
                normalizedCity + " Garden Stay", normalizedCity + " Express Inn");
        List<BigDecimal> rates = Arrays.asList(BigDecimal.valueOf(3200), BigDecimal.valueOf(2400),
                BigDecimal.valueOf(1700));
        List<String> ratings = Arrays.asList("4.6", "4.3", "4.0");
        List<String> descriptions = Arrays.asList("Comfortable premium stay near the city centre",
                "Relaxed rooms with breakfast included", "Practical stay with easy station access");
        return java.util.stream.IntStream.range(0, names.size()).mapToObj(index -> new HotelResponse(
                citySlug + "-hotel-" + (index + 1), names.get(index), normalizedCity,
                "Near " + normalizedCity + " Railway Station", rates.get(index), ratings.get(index),
                descriptions.get(index))).collect(Collectors.toList());
    }

    public List<HotelBooking> findBookings(String ownerEmail) {
        return hotelBookingRepository.findAllByOwnerEmailIgnoreCaseOrderByCreatedAtDesc(ownerEmail);
    }

    @Transactional
    public HotelBooking createBooking(HotelBookingRequest request, String ownerEmail) {
        LocalDate today = LocalDate.now();
        if (request.getCheckIn().isBefore(today)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-in date cannot be in the past");
        }
        if (!request.getCheckOut().isAfter(request.getCheckIn())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-out must be after check-in");
        }
        HotelResponse hotel = trainRepository.findAll().stream()
                .map(Train::getDestination).map(HotelService::cityName).distinct()
                .flatMap(city -> findHotels(city).stream())
                .filter(listing -> listing.getId().equals(request.getHotelId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found"));
        long nights = ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
        BigDecimal totalPrice = hotel.getNightlyRate().multiply(BigDecimal.valueOf(nights));
        return hotelBookingRepository.save(new HotelBooking(hotel.getId(), hotel.getName(), hotel.getCity(),
                hotel.getAddress(), request.getCheckIn(), request.getCheckOut(), request.getGuests(),
                request.getGuestName(), request.getEmail(), ownerEmail, totalPrice));
    }

    private static String cityName(String station) {
        return station.replaceFirst(" (Central|Junction|Terminal|City)$", "");
    }
}
