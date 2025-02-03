package com.vlad.kuzhyr.rideservice.persistence.repository;

import com.vlad.kuzhyr.rideservice.persistence.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

  Optional<Address> findByAddressName(String addressName);

}
