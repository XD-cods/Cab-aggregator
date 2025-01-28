package com.vlad.kuzhyr.rideservice.service.impl;

import com.vlad.kuzhyr.rideservice.exception.RideNotFoundException;
import com.vlad.kuzhyr.rideservice.exception.RidesNotFoundByDriverIdException;
import com.vlad.kuzhyr.rideservice.exception.RidesNotFoundByPassengerIdException;
import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import com.vlad.kuzhyr.rideservice.persistence.repository.RideRepository;
import com.vlad.kuzhyr.rideservice.service.RideService;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.rideservice.utility.mapper.RideMapper;
import com.vlad.kuzhyr.rideservice.web.request.RideRequest;
import com.vlad.kuzhyr.rideservice.web.request.UpdateRideStatusRequest;
import com.vlad.kuzhyr.rideservice.web.response.RideResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {
  private final RideRepository rideRepository;
  private final RideMapper rideMapper;

  @Override
  public RideResponse getRideById(Long id) {
    Ride existingRide = rideRepository.findById(id)
            .orElseThrow(() -> new RideNotFoundException(
                    ExceptionMessageConstant.RIDE_NOT_FOUND_MESSAGE.formatted(id)
            ));

    return rideMapper.toResponse(existingRide);
  }

  @Override
  public List<RideResponse> getAllRidesByDriverId(Long driverId, Integer offset, Integer limit) {
    if (!rideRepository.existsRidesByDriverId(driverId)) {
      throw new RidesNotFoundByDriverIdException(
              ExceptionMessageConstant.RIDES_NOT_FOUND_BY_DRIVER_ID_MESSAGE.formatted(driverId)
      );
    }

    List<Ride> existingRidesByDriverId = rideRepository
            .findByDriverId(driverId, PageRequest.of(offset, limit));

    return existingRidesByDriverId.stream()
            .map(rideMapper::toResponse)
            .collect(Collectors.toList());
  }

  @Override
  public List<RideResponse> getAllRidesByPassengerId(Long passengerId, Integer offset, Integer limit) {
    if (!rideRepository.existsRidesByPassengerId(passengerId)) {
      throw new RidesNotFoundByPassengerIdException(
              ExceptionMessageConstant.RIDES_NOT_FOUND_BY_PASSENGER_ID_MESSAGE.formatted(passengerId)
      );
    }

    List<Ride> existingRidesByPassengerId = rideRepository
            .findByPassengerId(passengerId, PageRequest.of(offset, limit));

    return existingRidesByPassengerId.stream()
            .map(rideMapper::toResponse)
            .collect(Collectors.toList());
  }

  @Override
  public RideResponse updateRide(Long id, RideRequest rideRequest) {
    Ride existingRide = rideRepository.findById(id)
            .orElseThrow(() -> new RideNotFoundException(
                    ExceptionMessageConstant.RIDE_NOT_FOUND_MESSAGE.formatted(id)
            ));

    rideMapper.updateFromRequest(rideRequest, existingRide);
    Ride savedRide = rideRepository.save(existingRide);
    return rideMapper.toResponse(savedRide);
  }

  @Override
  public RideResponse updateRideStatus(Long id, UpdateRideStatusRequest rideRequest) {
    Ride existingRide = rideRepository.findById(id)
            .orElseThrow(() -> new RideNotFoundException(
                    ExceptionMessageConstant.RIDE_NOT_FOUND_MESSAGE.formatted(id)
            ));

    RideStatus rideStatus = rideRequest.rideStatus();
    switch (rideStatus) {
      case ON_THE_WAY -> existingRide.setStartTime(LocalDateTime.now());
      case COMPLETED -> existingRide.setFinishTime(LocalDateTime.now());
    }

    existingRide.setRideStatus(rideStatus);
    Ride savedRide = rideRepository.save(existingRide);
    return rideMapper.toResponse(savedRide);
  }

  @Override
  public RideResponse createRide(RideRequest rideRequest) {
    Ride ride = rideMapper.toEntity(rideRequest);
    Ride savedRide = rideRepository.save(ride);
    return rideMapper.toResponse(savedRide);
  }

}
