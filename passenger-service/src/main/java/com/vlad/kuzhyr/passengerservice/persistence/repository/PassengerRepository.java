package com.vlad.kuzhyr.passengerservice.persistence.repository;

import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    Boolean existsPassengerByEmailAndIsEnabledTrue(String email);

    Boolean existsPassengerByPhoneAndIsEnabledTrue(String phone);

    Optional<Passenger> findPassengerByIdAndIsEnabledTrue(Long id);

}
