package com.stlabs.railway.repository;

import com.stlabs.railway.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainRepository extends JpaRepository<Train, Long> {
}
