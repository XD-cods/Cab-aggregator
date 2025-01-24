package com.vlad.kuzhyr.passengerservice.persistence.repository;

import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
  Boolean existsPassengerByEmail(String email);

  Optional<Passenger> findPassengerByEmail(String email);

  Optional<Passenger> findPassengerByIdAndIsEnabledTrue(Long id);

  Boolean existsPassengersByEmailOrPhone(String email, String phone);

}
