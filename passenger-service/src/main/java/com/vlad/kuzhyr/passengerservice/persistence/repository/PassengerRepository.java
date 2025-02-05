package com.vlad.kuzhyr.passengerservice.persistence.repository;

import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

  Boolean existsPassengerByEmailAndIsEnabledTrue(String email);

  Boolean existsPassengerByPhoneAndIsEnabledTrue(String phone);

  Optional<Passenger> findPassengerByIdAndIsEnabledTrue(Long id);

}
