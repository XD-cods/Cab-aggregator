package com.vlad.kuzhyr.driverservice.persistence.repository;

import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

  Optional<Driver> findDriverByIdAndIsEnabledTrue(Long id);

  Boolean existsDriverByEmailAndIsEnabledTrue(String driverRequestEmail);

  Boolean existsDriverByPhoneAndIsEnabledTrue(String driverRequestPhone);

}