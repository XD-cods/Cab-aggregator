package com.vlad.kuzhyr.driverservice.persistence.repository;

import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findDriverByIdAndIsEnabledTrue(Long id);

    Boolean existsDriverByEmailAndIsEnabledTrue(String driverRequestEmail);

    Boolean existsDriverByPhoneAndIsEnabledTrue(String driverRequestPhone);

}