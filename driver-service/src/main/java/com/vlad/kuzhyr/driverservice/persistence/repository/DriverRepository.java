package com.vlad.kuzhyr.driverservice.persistence.repository;

import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}