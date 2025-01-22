package com.vlad.kuzhyr.passengerservice.persistence.repository;

import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
  Boolean existsPassengerByEmail(@Email @NotEmpty String email);

  Optional<Passenger> findPassengerByEmail(@Email @NotEmpty String email);

}
