package com.stlabs.railway.repository;

import com.stlabs.railway.model.TrainAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainAvailabilityRepository extends JpaRepository<TrainAvailability, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select availability from TrainAvailability availability where availability.train.id = :trainId and availability.travelDate = :travelDate")
    Optional<TrainAvailability> findForUpdate(@Param("trainId") Long trainId, @Param("travelDate") LocalDate travelDate);

    List<TrainAvailability> findAllByTravelDate(LocalDate travelDate);
}
