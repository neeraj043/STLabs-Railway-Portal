package com.stlabs.railway.repository;

import com.stlabs.railway.model.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {
    List<HotelBooking> findAllByOwnerEmailIgnoreCaseOrderByCreatedAtDesc(String ownerEmail);
}
