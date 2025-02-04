package com.vlad.kuzhyr.rideservice.persistence.repository;

import com.vlad.kuzhyr.rideservice.persistence.entity.Address;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByAddressName(String addressName);

}
