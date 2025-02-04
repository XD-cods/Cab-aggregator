package com.vlad.kuzhyr.driverservice.persistence.repository;

import com.vlad.kuzhyr.driverservice.persistence.entity.Car;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findCarByIdAndIsEnabledTrue(Long id);

    Boolean existsCarByCarNumberAndIsEnabledTrue(String carNumber);

    Page<Car> findByIsEnabledTrue(Pageable pageable);

}