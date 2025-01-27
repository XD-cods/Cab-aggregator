package com.vlad.kuzhyr.driverservice.persistence.repository;

import com.vlad.kuzhyr.driverservice.persistence.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

  Optional<Car> findCarByIdAndIsEnabledTrue(Long id);

  Boolean existsCarByCarNumberAndIsEnabledTrue(String carNumber);

}