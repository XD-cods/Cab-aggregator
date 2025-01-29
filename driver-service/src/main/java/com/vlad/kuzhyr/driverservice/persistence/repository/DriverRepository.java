package com.vlad.kuzhyr.driverservice.persistence.repository;

import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

  Optional<Driver> findDriverByIdAndIsEnabledTrue(Long id);

  Boolean existsDriverByEmailAndIsEnabledTrue(String driverRequestEmail);

  Boolean existsDriverByPhoneAndIsEnabledTrue(String driverRequestPhone);

  Page<Driver> findByIsEnabledTrue(Pageable pageable);
}