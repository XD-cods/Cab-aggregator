package com.vlad.kuzhyr.driverservice.persistence.repository;

import com.vlad.kuzhyr.driverservice.persistence.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}